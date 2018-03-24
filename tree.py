#!/usr/bin/python
# -*- coding: UTF-8 -*-

"""
list a directory in tree way.

children(path):
    map(lambda name: tree(path, name), listdir(path))

tree(parent, dir_name):
    if is_file(parent, dir_name):
        return {'name': dir_name, 'children': []}
    else:
        children = children(join(parent, dir_name))
        return {'name': dir_name, 'children': children}
"""
import os, sys

I_branch = "│   "
T_branch = "├── "
L_branch = "└── "
SPACER   = "    "

def children(path):
    return map(lambda filename: tree(path, filename), os.listdir(path))

def tree(parent, dir_name):
    p = os.path.join(parent, dir_name)
    is_file = os.path.isfile(p) 
    the_children = [] if is_file else children(p)
    return {'name': dir_name, 'children': the_children}

def render_tree(tr):
    name = tr['name']
    children = tr['children']
    return [name] + reduce(lambda l, r: l+r, 
                           map(render(len(children)), enumerate(children)), 
                           [])

def render(length):
    def prefix((index, child)):
        is_last = index == length-1 
        prefix_first = L_branch if is_last else T_branch
        prefix_rest = SPACER if is_last else I_branch
        tr = render_tree(child)
        head = prefix_first + tr[0]
        tail = map(lambda t: prefix_rest + t, tr[1:])
        return [head] + tail
    return prefix

#print '\n'.join(render_tree(tree('', ".")))

if __name__ == "__main__":
    print '\n'.join(render_tree(tree('', sys.argv[1])))
