# easy-search
一个使用java语言实现的文本搜索引擎

## 数据结构类
	Index
	--invertIndex  //TreeMap<String, TreeSet<Long>> 结构，key是token，value是文档id列表
	--docMap    //HashMap<Long, Doc> 结构，key是文档id,value是文档实体类(包含文档本地保存地址)

## 数据处理类
	Indexer
	--add(List<Doc> docs)   //向索引添加文档，当可用内存小于一定阈值时，自动触发hardCommit()
	--hardCommit()  //硬提交，将内存中的invertIndex与硬盘中的invertIndex进行合并后写入到硬盘；将内存中的docMap覆盖到硬盘。

	Searcher
	--search(String word)   //从索引中搜索文档

## 测试类
	ClientTest
