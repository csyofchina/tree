const path = require('path')
const fs = require('fs')
const I_branch = '│   '
const T_branch = '├── '
const L_branch = '└── '
const SPACER   = '    '

function children(path) {
    return fs.readdirSync(path).map(filename => tree(path, filename))
}

function tree(parentDir, dirName) {
    let realPath = path.join(parentDir, dirName)
    let isDir = fs.statSync(realPath).isDirectory()
    return {name: dirName, children: isDir ? children(realPath) : []}
}

function prefix(len) {
    return (tr, index) => {
        let isLast = len == index + 1
        let prefixFirst = isLast ? L_branch : T_branch
        let prefixRest = isLast ? SPACER : I_branch
        let [head, ...tail]= renderTree(tr)

        return [prefixFirst + head].concat(tail.map(name => prefixRest + name))
    }
}

function renderTree({name: name, children: children}) {
    return [name]
        .concat(children
            .map(prefix(children.length))
            .reduce((l, r) => l.concat(r), []))
}

console.log(renderTree(tree('', process.argv[2])).join('\n'))
