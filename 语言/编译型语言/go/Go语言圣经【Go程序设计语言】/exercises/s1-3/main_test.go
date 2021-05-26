package main

import "testing"

func BenchmarkPrintln(b *testing.B) {
	for i := 0; i < b.N; i++ {
		printlnTest()
	}
}

func BenchmarkJoin(b *testing.B) {
	for i := 0; i < b.N; i++ {
		joinTest()
	}
}