# 单词提取软件
从任意文本格式的文件中读取数据，去除非单词的数据，只保留单词，提取出来讲结果存储到exportNotHandledWordDir中
文件为txt 格式。 将产生的txt上传到 知米背单词的app上，进行背单词即可。

## 程序说明
- 正则去除非英文的字符
- 采用的正则表达式为 `[^a-zA-Z ]`
- 提取文本中的生词
- 知米背单词 导入提取出来的生词
- 导出的格式 每个单词 一行 符合知米背单词 要求的导入格式
### 目录结构

```$

├── exportNotHandledWordDir
├── handledWordDir
├── notHandledWordDir

```
- exportNotHandledWordDir  
导出的结果文件  
    - UTF-8 格式的文本文件
    - 每个单词一行
    - 换行符为 `\n`
    
- handledWordDir  
已经掌握的单词，任意格式的文本，需要保证单词与单词之间以 空格作为间隔 即可
    - UTF-8 格式的文本文件
    - 空格作为单词与单词之间的分割符
    - 可任意行
    - 分割符 whitespace ` `
- notHandledWordDir  
需要分析提取的文本，格式要求如下
    - utf-8 格式的文本文件
    - 单词与单词之间 空格 作为分隔符
    - 分割符 whitespace ` `
    
- 其他说明
    - 输出结果会自动去重
    