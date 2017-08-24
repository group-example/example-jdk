  > 学习了这多IO流对象，到底怎么用呢？感觉对象太多，用的时候很乱，现在我们来总结下IO流的操作规律。
 
## 流的使用步骤	 

### 一、确定数据的【类型】和【传输方向】：
操作的设备上的数据是字节还是文本,是出去还是进来？
<table>
    <tr>
        <th></th>
        <th>数据要出去</th>
        <th>数据要进来</th>
    </tr>
    <tr>
        <td>数据是字节</td>
        <td>OutputStream</td>
        <td>InputStream</td>
    </tr>
    <tr>
        <td>数据是字符</td>
        <td>Writer</td>
        <td>Reader</td>
    </tr>
</table>


### 二、确定数据【所在的设备类型】（介质）：
数据来源的设备/数据目的地的设备，是什么类型？


<table>
    <tr>
        <th></th>
        <th>设备类型</th>
    </tr>
    <tr>
        <td>硬盘</td>
        <td>File开头</td>
    </tr>
    <tr>
        <td>内存</td>
        <td>ByteArray开头（字节）；CharArray开头（字符）</td>
    </tr>
    <tr>
        <td>进程</td>
        <td>Piped开头</td>
    </tr>
    <tr>
        <td>键盘/显示器</td>
        <td>System.in/System.out</td>
    </tr>
</table>


### 三、确定额外的处理：
是否需要额外的处理？（额外的处理可以更高效、更方便的操作数据）
						
 | 如果需要缓冲 |  如果需要转换 | 如果要控制打印|如果数据是对象（需序列化）| 如果数据是基本数据类型|
|-----   |-----|  -----| ------| ------|
 | BufferedInputStream  | InputStreamReader|PrintWriter |ObjectInputStream|DataInputStream|
| BufferedOutputStream | OutputStreamWriter | PrintStream |ObjectOutputStream|DataOutputStream|
|  BufferedReader     |   |  |  |
|  BufferedWriter     |   |  |  |

> 注意：对象化流、数据化流，只适用于字节型数据。