#!/usr/bin/env elixir

defmodule Tree do
  def main([dir | _]) do
    dir |> tree_format |> render_tree |> Enum.join("\n") |> IO.puts
  end

  defp children(parent) do
   children(parent |> File.ls, parent)
  end

  defp children({:error, _}, parent) do
    []
  end

  defp children({:ok, sub_dir}, parent) do
      sub_dir |> Enum.map(fn child -> tree_format(parent, child) end)
  end

  defp tree_format(parent_dir \\ ".", dir_name) do
    %{:name => dir_name, :children => Path.join(parent_dir, dir_name) |> children}
  end

  defp decorate(is_last?, [parent | children]) do
    prefix_first = (if (is_last?), do: "└── ", else: "├── ")
    prefix_rest = (if (is_last?), do: "    ", else: "│   ")
    [prefix_first <> parent | children |> Enum.map(fn child -> prefix_rest <> child end)]
  end

  defp render_tree(%{name: dir_name, children: children}) do
    [dir_name 
     | children 
     |> Enum.with_index(1)
     |> Enum.map(fn {child, index} -> decorate(length(children) == index, render_tree(child)) end) 
     |> Enum.flat_map(fn x -> x end)]
  end

end

Tree.main(System.argv)
