import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.File

// Preamble:  I have saved a complete html page (html + images + the rest) that contains images of bead packs.
// Those image files have names different from the bead codes that they represent.  The html file contains "img"
// tags with "alt" attribute that contains bead code.
// The goal of this script is to extract bead codes from that attribute, find the image file and save it with required
// name.

/**
 * Entry point for the program execution
 * @param args list of files to process
 */
fun main(args: Array<String>) {
    args.forEach(::elaborateFile)

}

/**
 * Inspects given file.
 * Parse all image tags in a file with given name.
 */
fun elaborateFile(fileName: String) {
    val file = File(fileName)
    val dir = file.parent + '\\'
    val doc = Jsoup.parse(file, "ASCII")
    val images = doc.body().select("img")
    images.forEach { image -> elaborateImage(image, dir) }
}

/**
 * Elaborate the image tag.
 */
fun elaborateImage(image: Element, baseDir: String) {
    val alt = image.attr("alt")
    val src = image.attr("src")
    if (alt.isEmpty()) {
        println("no alt for image $src")
        return
    }
    val pattern = Regex("\\d{5}(/\\d)?")
    val matches = pattern.find(alt, 0)
    if (matches != null) {
        val oldPath = baseDir + src.replace('/', '\\')
        val newPath = baseDir + matches.value.replace('/', '-') + ".jpg"
        renameFile(oldPath, newPath)
    } else println("no color code is found in $alt")
}

/**
 * Save a file with new name.
 * @param oldFile full path to the file. Assume it exists.
 * @param newFile full path to the new location
 */
fun renameFile(oldName: String, newName: String) {
    val oldFile = File(oldName)
    if (!oldFile.exists()) {
        println("no file $oldName")
    }
    val newFile = File(newName)
    if (newFile.exists()) {
        println("file $newName already exists!")
    } else {
        oldFile.copyTo(newFile)
    }
}
