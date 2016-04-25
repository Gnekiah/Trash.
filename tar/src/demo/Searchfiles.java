public class IndexJob {

    private static String loadFileToString(File file) {
	    return "读取的文件内容";
	}

    public static void createIndex(String inputDir) {
		Directory directory = null;
		IndexWriter writer = null;
		IndexSearcher searcher = null;
	
		try {
			
			//从配置文件中读取索引存放路径
			String indexPath = IndexUtil.getValue(IndexUtil.INDEX_ROOT);
			directory = FSDirectory.open(new File(indexPath));
			File indexFile = new File(indexPath + "\\segments.gen");
			

				//新建索引信息
				writer = new IndexWriter(directory, new IKAnalyzer(), true,
						IndexWriter.MaxFieldLength.LIMITED);
				writer.setMergeFactor(1000);
				writer.setMaxBufferedDocs(100);
				writer.setMaxMergeDocs(Integer.MAX_VALUE);


			File fileDir = new File(inputDir);
			File[] files = fileDir.listFiles();
			if (files.length > 1) {

				//索引文件中存储的索引字段的定义
				Field fieldName = new Field("id", "", Field.Store.YES,
						Field.Index.UN_TOKENIZED);
				Field fieldPath = new Field("path", "", Field.Store.YES,
						Field.Index.NO);
				Field fieldContent = new Field("content", "", Field.Store.COMPRESS,
						Field.Index.ANALYZED);
				Document doc = null;

				//遍历文件根目录下所有子目录并创建索引
				for (int i = 0; i < files.length - 1; i++) {
					if (files[i].isDirectory()) {
						
						//这里需要根据当前文件夹的命名规律和上次建索引后记录的文件夹名字比较
						//避免出现对文件重复创建索引
						
								File fileDirs = new File(files[i]
										.getAbsolutePath());
								File[] file = fileDirs.listFiles();
								for (int j = 0; j < file.length; j++) {
									
									String fileName = file[j].getName();

									String lastName = "";
									if (fileName.lastIndexOf(".") != -1) {
										lastName = fileName
												.substring(fileName
														.lastIndexOf("."));
									}
									
									if (lastName.equals(".txt")) {
										doc = new Document();
										fieldName.setValue(fileName.substring(0, fileName.indexOf(".")));
										doc.add(fieldName);
										fieldPath.setValue(file[j]
												.getAbsolutePath());
										doc.add(fieldPath);
										fieldContent.setValue(loadFileToString(file[j]));
										doc.add(fieldContent);
										writer.addDocument(doc);
									}
								}
					}
				}
			}

		} catch (Exception e) {
			//清空writer中的索引信息，否则writer在close时会将信息写入索引文件
			writer = null;
			e.printStackTrace();
		} finally {

			if (searcher != null) {
				try {
					searcher.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (writer != null) {
				try {
					//优化索引并合并索引文件
					writer.optimize()；
					writer.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

    public static void mergeIndex() {
        IndexWriter writer = null;
        Directory toDirectory = null;
        Directory fromDirectory = null;
        try {
            File from = new File(INDEX_THREAD1);
            File to = new File(IndexUtil.getValue(IndexUtil.INDEX_ROOT));
            toDirectory = FSDirectory.open(to);
            fromDirectory = FSDirectory.open(from);
            writer = new IndexWriter(toDirectory, new IKAnalyzer(), false, IndexWriter.MaxFieldLength.LIMITED);
            writer.setMergeFactor(100);
            writer.setMaxBufferedDocs(100);
            writer.setMaxMergeDocs(Integer.MAX_VALUE);
            writer.addIndexes(IndexReader.open(fromDirectory));
            writer.optimize();
            writer.close();
        } catch (Exception e) {
            writer = null;
            e.printStackTrace();
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (Exception e) {

            }
            if (toDirectory != null) {
                try {
                    toDirectory.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (fromDirectory != null) {
                try {
                    fromDirectory.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

public class Search {

    public void indexSearch(String searchType, String searchKey) {
        String INDEX_STORE_PATH = "g:\\index";
        Directory directory = null;
        IndexSearcher searcher = null;

        try {
            directory = FSDirectory.open(new File(INDEX_STORE_PATH));
            searcher = new IndexSearcher(directory, true);
            searcher.setDefaultFieldSortScoring(true, false);
            searcher.setSimilarity(new IKSimilarity());
            Query query = IKQueryParser.parse(searchType, searchKey);
            System.out.println("查询条件为：" + query);

            // 索引排序条件
            SortField[] sortfield = new SortField[] { SortField.FIELD_SCORE, new SortField(null, SortField.DOC, true) };
            Sort sort = new Sort(sortfield);

            TopDocs topDocs = searcher.search(query, null, 10, sort);
            System.out.println("检索到总数：" + topDocs.totalHits);
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;

            Analyzer analyzer = new IKAnalyzer();
            for (int i = 0; i < scoreDocs.length; i++) {
                Document targetDoc = searcher.doc(scoreDocs[i].doc);
                TokenStream tokenStream = analyzer.tokenStream("", new StringReader(targetDoc.get("content")));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
