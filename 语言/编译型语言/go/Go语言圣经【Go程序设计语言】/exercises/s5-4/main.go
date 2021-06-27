// 扩展visit函数，使其能够处理其他类型的结点，如images、scripts和style sheets。
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
	if n.Type == html.ElementNode && (n.Data == "a" || n.Data == "img" || n.Data == "link" || n.Data == "script") {
		for _, a := range n.Attr {
			if a.Key == "href" || a.Key == "src" {
				fmt.Println(a.Val)
			}
		}
	}
	for c := n.FirstChild; c != nil; c = c.NextSibling {
		visit(c)
	}
}