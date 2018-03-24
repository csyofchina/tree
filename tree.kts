import java.io.File

val I_branch = "│   "
val T_branch = "├── "
val L_branch = "└── "
val SPACER   = "    "

data class Entry (val name: String, val children: List<Entry>)

fun children(path: File): List<Entry> {
    return path.listFiles().map {tree(it)}
}

fun tree(path: File): Entry {
    val isDir = path.isDirectory()
    return Entry(path.getName(), if(isDir) children(path) else listOf<Entry>())
}

fun renderTree(tree: Entry): List<String> {
    val name = tree.name
    val children = tree.children

    return listOf(name) + children.mapIndexed { i, e -> prefix(children.size)(i, e) }.fold(listOf<String>()) {l, r -> l + r}
}

fun prefix(size: Int): (Int, Entry) -> List<String> {
    return {index, entry ->
        val isLast = index + 1 == size
        val prefixFirst = if(isLast) L_branch else T_branch
        val prefixRest = if(isLast) SPACER else I_branch
        val subTree = renderTree(entry)

        listOf(prefixFirst + subTree.first()) + subTree.drop(1).map {t -> prefixRest + t}
    } 
}

println(renderTree(tree(File(args[0]))).joinToString("\n"))
