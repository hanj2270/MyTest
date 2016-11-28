package blobStore;

public interface IBlobStore {
	 public String getType();

	    /* 持久化 */
	    public int load();
	    public int save();

	    /* 将所有Key/Value复制到另一个Store（可以是不同类型的Store） */
	    public int copyTo(IBlobStore other);

	    /* 遍历与查找 */
	    public int size();                              //获取容量
	    public String[] listAliases();                  //获取名称列表
	    public boolean containsAlias(String alias);     //判断存在性
	    public boolean isEncrypted(String alias);       //判断条目是否加密
	    public String findAlias(byte[] value);              //根据二进制内容查找名称

	    /* 明文数据操作 */
	    public int setBlob(String alias, String value);
	    public int setBlob(String alias, byte[] value);
	    public byte[] getBlob(String alias);
	    public String getBlobAsString(String alias);

	    /* 密文数据操作（固定使用SM4或AES256-CBC加密，密钥使用password的MD5摘要值） */
	    public int setEncryptedBlob(String alias, String entryPassword, String value);
	    public int setEncryptedBlob(String alias, String entryPassword, byte[] value);
	    public byte[] getEncryptedBlob(String alias, String entryPassword);
	    public String getEncryptedBlobAsString(String alias, String entryPassword);

	    /* 删除 */
	    int deleteBlob(String alias);
	    int clearAll();
	    
}
