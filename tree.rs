use std::env;
use std::path::Path;
use std::fs;

struct Entry {
    name: String,
    children: Vec<Entry> 
}

fn main() {
    let args: Vec<String> = env::args().collect();
    println!("{}", render_tree(&tree(Path::new(&args[1]))).join("\n"));
}

fn children(dir: &Path) -> Vec<Entry> {
    fs::read_dir(dir)
        .expect("unable to read dir")
        .into_iter()
        .map(|e| e.expect("unable to get entry").path())
        .map(|e| tree(&e))
        .collect()
}

fn tree(path: &Path) -> Entry {
    Entry{
        name: path.display().to_string(),
        children: if path.is_dir() {
            children(path)
        } else {
            Vec::new()
        }
    }
}

fn render_tree(tree: &Entry) -> Vec<String> {
    let mut names = vec![String::from(&tree.name)];
    let children = &tree.children;
    let children: Vec<_> = children
        .iter()
        .enumerate()
        .map(|(i, child)| decorate(children.len() - 1 == i, render_tree(child)))
        .flatten()
        .collect();
    
    names.extend(children);

    names
}

fn decorate(is_last: bool, children: Vec<String>) -> Vec<String> {
    let i_branch = "│   ";
    let t_branch = "├── "; 
    let l_branch = "└── ";
    let   spacer = "    ";

    let prefix_first = if is_last { l_branch } else { t_branch };

    let prefix_rest = if is_last { spacer } else { i_branch };

    let mut first = vec![format!("{}{}", prefix_first, children[0])];

    first.extend(children[1..].iter().map(|child| format!("{}{}", prefix_rest, child)).collect::<Vec<_>>());

    first
}
