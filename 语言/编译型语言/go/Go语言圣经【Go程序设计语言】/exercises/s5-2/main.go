// 编写函数，记录在HTML树中出现的同名元素的次数。
package main

import (
	"fmt"
	"golang.org/x/net/html"
	"os"
	"path/filepath"
	"runtime"
)

var res map[string]int

func main() {
	_, fullFilename, _, _ := runtime.Caller(0)
	path := filepath.Dir(fullFilename)
	file, err := os.OpenFile(path+"/text.html", os.O_RDWR, 0777)
	doc, err := html.Parse(file)

	if err != nil {
		_, _ = fmt.Fprintf(os.Stderr, "findlinks1: %v\n", err)
		os.Exit(1)
	}
	res = make(map[string]int)
	visit(doc)
	fmt.Println(res)
}

// visit appends to links each link found in n and returns the result.
func visit(n *html.Node) {
	if n.Type == html.ElementNode {
		if _, ok := res[n.Data]; !ok {
			res[n.Data] = 0
		}
		res[n.Data]++
	}
	for c := n.FirstChild; c != nil; c = c.NextSibling {
		visit(c)
	}
}
