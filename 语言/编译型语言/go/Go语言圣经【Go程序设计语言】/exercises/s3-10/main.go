// 编写一个非递归版本的comma函数，使用bytes.Buffer代替字符串链接操作。
package main

func comma(s string) string {
	n := len(s)
	if n <= 3 {
		return s
	}
	return comma(s[:n-3]) + "," + s[n-3:]
}
