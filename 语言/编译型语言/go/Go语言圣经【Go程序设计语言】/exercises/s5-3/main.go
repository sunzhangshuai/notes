// 编写函数输出所有text结点的内容。注意不要访问<script>和<style>元素，因为这些元素对浏览者是不可见的。
package main

import (
	"fmt"
	"golang.org/x/net/html"
	"os"
	"path/filepath"
	"runtime"
)

func main() {
	_, fullFilename, _, _ := runtime.Caller(0)
	path := filepath.Dir(fullFilename)
	file, err := os.OpenFile(path+"/text.html", os.O_RDWR, 0777)
	doc, err := html.Parse(file)

	if err != nil {
		_, _ = fmt.Fprintf(os.Stderr, "findlinks1: %v\n", err)
		os.Exit(1)
	}
	visit(doc)
}

// visit appends to links each link found in n and returns the result.
func visit(n *html.Node) {
	if n.Type == html.TextNode && n.Data != "script" && n.Data != "style" {
		fmt.Println(n.Data)
	}
	for c := n.FirstChild; c != nil; c = c.NextSibling {
		if n.Data == "script" || n.Data == "style" {
			continue
		}
		visit(c)
	}
}