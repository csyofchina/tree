package main

import (
	"fmt"
	"io/ioutil"
	"os"
	"path"
	"strings"
)

const (
	I_branch = "│   "
	T_branch = "├── "
	L_branch = "└── "
	SPACER   = "    "
)

type entry struct {
	name     string
	children []entry
}

func (e entry) String() string {
	if len(e.children) == 0 {
		return e.name
	} else {
		s := e.name
		for _, child := range e.children {
			s += child.String()
		}

		return s
	}
}

func Children(path string) []entry {
	result := []entry{}
	files, _ := ioutil.ReadDir(path)
	for _, f := range files {
		result = append(result, Tree(path, f.Name()))
	}

	return result
}

func Tree(parent, dirName string) entry {
	realPath := path.Join(parent, dirName)
	theChildren := []entry{}
	if f, ok := os.Stat(realPath); ok == nil {
		if f.IsDir() {
			theChildren = Children(realPath)
		}
	}
	return entry{name: dirName, children: theChildren}
}

func RenderTree(e entry) []string {
	name := e.name
	children := e.children
	result := []string{name}

	for index, child := range children {
		subTree := RenderTree(child)
		prefixFirst := T_branch
		prefixRest := I_branch
		if index == len(children)-1 {
			prefixFirst = L_branch
			prefixRest = SPACER
		}

		result = append(result, prefixFirst+subTree[0])

		for _, sub := range subTree[1:] {
			result = append(result, prefixRest+sub)
		}
	}
	return result
}

func main() {
	fmt.Println(strings.Join(RenderTree(Tree("", os.Args[1])), "\n"))
}
