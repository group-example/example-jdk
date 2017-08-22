  > 学习了这多IO流对象，到底怎么用呢？感觉对象太多，用的时候很乱，现在我们来总结下IO流的操作规律。
 
## 流的使用步骤	 

### 明确一：
要操作的设备上的数据是字节还是文本？
						
|  如果是文本  | 如果是字节 | 
|-----   |-----|
| Writer | OutputStream  | 
| Reader      | InputStream  | 	
 

### 明确二：
要操作的数据是要从设备输出，还是要输入到设备。
						
|  如果是输入  | 如果是输出 | 
|-----   |-----|
| InputStream | OutputStream  | 
| Reader      | Writer  | 


### 明确三：
明确数据所在的设备类型。

|  如果是输入  | 如果是输出 | 
|-----   |-----|
| 硬盘：文件  File开头 | 硬盘：文件  File开头  | 
| 内存：字节数组，字符串      | 内存：字节数组，字符串  | 	 
| 键盘：System.in      | 屏幕：System.out  | 	 
| 网络：Socket      | 网络：Socket  | 


### 明确四：
是否需要额外功能？
						
|  如果需要转换  | 如果需要缓冲 | 如果需要数组 | 如果需要管道 |
|-----   |-----|  -----| ------|
| InputStreamReader | BufferedInputStream  | ByteArrayInputStream | PipedInputStream |
| OutputStreamWriter| BufferedOutputStream  | ByteArrayOutputStream |PipedOutputStream |
| | BufferedReader  |CharArrayReader  |PipedReader  |
| | BufferedWriter  |CharArrayWriter  |PipedWriter  |
