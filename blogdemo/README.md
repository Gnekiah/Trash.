#blogdemo
*based on tornado*. ��̬����ͨ��nginxӳ�䵽pythonӦ�ã���̬����ֱ�ӷ��ؾ�̬�ļ���

### files
templates htmlģ��  
static ��̬�ļ�λ��  
static/background ��������ҳ����ʾͼƬ  
static/files �ϴ��ļ���Ŀ¼  
static/cache ����Ŀ¼  
static/blog blog���ݡ������Լ����ݿ��ļ�Ŀ¼  
static/cland cland���ݡ������Լ����ݿ��ļ�Ŀ¼  
log.log ��־�ļ�  
config.conf �����ļ������õ�¼�������롢��ϣ��ֵ��cookie���е�

### router
IndexHandler ��ҳ    
BlogHandler ����չʾҳ   
ArticleHandler �����������ҳ  
ClandHandler Ԥ��Xչʾҳ  
ChapterHandler Ԥ��X���ҳ  
AboutHandler about  
LoginHandler ��¼  
LogoutHandler �ǳ�  
ManageHandler ����  
WritingHandler write blog or add cland page  
UploadImageHandler �ϴ�ͼƬ  
UploadHandler �ϴ�����   
ͨ����TODO������Լ��Ĺ��ܣ��Լ��Խ�htmlҳʵ����վ����

### markup�﷨
ʵ��markdown�Ĳ��ֹ��ܡ�~~����һ�ֺ�ɵȱ��ʵ�ַ�ʽ~~

    #�����
    ##��������
    
    >����
    >>��������
    
    ?? 
        �����
    ?!
    
    0?
    0. ��һ�� ����
    0. �ڶ���
    0. ������
    0!

    *?
    *. ��һ�� ����
    *. �ڶ���
    *. ������
    *!
    
    ==== �ָ���
    ![tupian](C:/9105080.jpg)
    %%����%%
    ^^б��^^
    $$������$$
    ``ctrl+a``
    @@google^https://google.com@@
    

        
            
