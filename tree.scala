import java.io._
val I_branch = "│   "
val T_branch = "├── "
val L_branch = "└── "
val SPACER   = "    "

case class Entry(name: String, children: List[Entry])

def children(path: File): List[Entry] = path.listFiles().toList.map((it: File) => tree(it))

def tree(path: File): Entry = Entry(path.getName(), if(path.isDirectory()) children(path) else List[Entry]())

def prefix(size: Int) = (index: Int, entry: Entry) => {
    val isLast = index + 1 == size
    val prefixFirst = if(isLast) L_branch else T_branch
    val prefixRest = if(isLast) SPACER else I_branch
    val subTree = renderTree(entry)
    List(prefixFirst + subTree.head) ++ subTree.tail.map(t => prefixRest + t)
}

def renderTree(tree: Entry): List[String] = {
    val name = tree.name
    val children = tree.children

    return List(name) ++ children
      .zipWithIndex
      .map({case (e: Entry, i: Int) => prefix(children.size)(i, e)})
      .fold(List[String]())((l, r) => l ++ r)
}


println(renderTree(tree(new File(args(0)))).mkString("\n"))
