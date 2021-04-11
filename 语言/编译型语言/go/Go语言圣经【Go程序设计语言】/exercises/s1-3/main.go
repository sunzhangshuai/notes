package main

import (
	"fmt"
	"os"
	"strings"
	"time"
)

func main() {
	start1 := time.Now()
	for i := 0; i <= 1000000; i++ {
		printlnTest()
	}
	end1 := time.Now()
	start := time.Now()
	for i := 0; i <= 1000000; i++ {
		joinTest()
	}
	end := time.Now()
	fmt.Println(end1.Sub(start1))
	fmt.Println(end.Sub(start))
}

func printlnTest()  {
	fmt.Println(os.Args[1:])
}

func joinTest()  {
	fmt.Println(strings.Join(os.Args[1:], " "))
}