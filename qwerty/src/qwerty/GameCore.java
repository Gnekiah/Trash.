package qwerty;

import java.io.IOException;
import java.util.Random;

public class GameCore {
    /**
     * 记录分数
     */
    private static int score;
    
    /**
     * 记录当前方块
     */
    private static int[][] currentCube=new int[5][5];
    
    /**
     * 记录下一块方块
     */
    private static  int[][] nextCube=new int[5][5];
    
    /**
     * 记录X位置
     */
    private static int LocationX;
    
    /**
     * 记录Y位置
     */
    private static int LocationY;
    
    /**
     * 记录矩阵内容
     */
    private static int[][] container=new int[35][20];
    
    /**
     * 调用声音
     */
    private Voice voice = new Voice();
    
    /**
     * 构造函数
     */
    public GameCore() {
        init();
    }
    
    /**
     * 重新初始化游戏
     */
    public void reInit() {
        init();
    }
    
    /**
     * 初始化
     */
    private void init(){
        score=0;
        LocationX=8;
        LocationY=0;
        createCube(currentCube);
        createCube(nextCube);
        for(int i=0;i<35;i++){
            for(int j=0;j<20;j++){
                container[i][j]=0;
            }
        }
    }
    
    /**
     * 判断旋转是否可行
     * @param cube 方块临时状态
     * @return 布尔值
     */
    private boolean judgeSwift(int[][] cube){
        boolean flag=true;
        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                if(cube[i][j]!=0){
                    if(j+LocationX<0||j+LocationX>=20||i+LocationY<0||i+LocationY>=35||container[i+LocationY][j+LocationX]!=0)
                        flag=false;
                }
            }
        }
        return flag;
    }
    
    /**
     * 判断移动是否可行
     * @param x x轴临时偏移量
     * @param y y轴临时偏移量
     * @return 布尔值
     */
    private boolean judgeMove(int x,int y){
        boolean flag=true;
        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                if(currentCube[i][j]!=0){
                    if(j+x<0||j+x>=20||i+y<0||i+y>=35||container[i+y][j+x]!=0)
                        flag=false;
                }
            }
        }
        return flag;
    }
    
    /**
     * 创建方块 
     */
    private void createCube(int[][] cube){
        /*
         * 初始化cube
         */
        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                cube[i][j]=0;
            }
        }
        /*
         * 生成随机类型的cube
         */
        int type=Math.abs(new Random().nextInt())%7+1;
        if(type==1){
            cube[1][2]=1;
            cube[2][2]=1;
            cube[3][2]=1;
            cube[4][2]=1;
        }
        if(type==2){
            cube[1][1]=2;
            cube[1][2]=2;
            cube[2][2]=2;
            cube[2][3]=2;
        }
        if(type==3){
            cube[1][3]=3;
            cube[1][2]=3;
            cube[2][2]=3;
            cube[2][1]=3;
        }
        if(type==4){
            cube[1][2]=4;
            cube[1][3]=4;
            cube[2][3]=4;
            cube[3][3]=4;
        }
        if(type==5){
            cube[1][2]=5;
            cube[1][1]=5;
            cube[2][1]=5;
            cube[3][1]=5;
        }
        if(type==6){
            cube[1][2]=6;
            cube[2][2]=6;
            cube[2][1]=6;
            cube[2][3]=6;
        }
        if(type==7){
            cube[1][1]=7;
            cube[1][2]=7;
            cube[2][1]=7;
            cube[2][2]=7;
        }
    }
    
    /**
     * 检验矩阵是否有完整的能够消除的行
     * @return 
     */
    private boolean checkContainer() {
        for(int i=0;i<5;i++){
            for(int j=0;j<20;j++){
                if(container[i][j]!=0){
                    return false;
                }
            }
        }
        int line=0;
        for(int i=5;i<35;i++){
            int sigline=1;
            for(int j=0;j<20;j++){
                if(container[i][j]==0){
                    sigline=0;
                }
            }
            if(sigline==1){
                for(int j=0;j<20;j++){
                    for(int k=i;k>=5;k--)
                        container[k][j]=container[k-1][j];
                }
            }
            line+=sigline;
        }
        score+=(line*2)*10;
        return true;
    }
    
    /**
     * 获取分数
     * @return 分数
     */
    public int getScore(){
        return score;
    }
    
    /**
     * 获取矩阵35*20
     * @return
     */
    public int[][] getContainer(){
        int[][] tempContainer=new int[35][20];
        for(int i=0;i<35;i++){
            for(int j=0;j<20;j++){
                tempContainer[i][j]=container[i][j];
            }
        }
        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                if(currentCube[i][j]!=0){
                    tempContainer[i+LocationY][j+LocationX]=currentCube[i][j];
                }
            }
        }
        return tempContainer;
    }
    
    /**
     * 获取下一个方块类型5*5
     * @return
     */
    public  int[][] getNextCube(){
        return nextCube;
    }
    
    /**
     * 旋转
     * @return
     * @throws IOException 
     */
    public  boolean swift() {
        int[][] tempCube=new int[5][5];
        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                tempCube[i][j]=currentCube[j][4-i];
            }
        }
        if(judgeSwift(tempCube)){
            currentCube=tempCube;
            return true;
        }
        return false;
    }
    
    /**
     * 向左移动
     * @return
     */
    public boolean leftWard() {
        if(judgeMove(LocationX-1,LocationY)){
            LocationX=LocationX-1;
            return true;
        }
        return false;
    }
    
    /**
     * 向右移动
     * @return
     */
    public boolean rightWard() {
        if(judgeMove(LocationX+1,LocationY)){
            LocationX=LocationX+1;
            return true;
        }
        return false;
    }
    
    /**
     * 向下移动
     * @return
     */
    public boolean downWard(){
        if(judgeMove(LocationX,LocationY+1)){
            LocationY=LocationY+1;
            return true;
        }
        return false;
    }
    
    /**
     * 触发游戏
     * @return false游戏结束
     * @throws IOException 
     */
    public boolean startGame() {
        if(!downWard()){
            for(int i=0;i<5;i++){
                for(int j=0;j<5;j++){
                    if(currentCube[i][j]!=0){
                        container[i+LocationY][j+LocationX]=currentCube[i][j];
                    }
                }
            }
            if(!checkContainer()){
                return false;
            }
            LocationX=8;
            LocationY=0;
            // 修改：注释掉下面一行，换成两个for循环赋值
            // currentCube=nextCube;
            for (int i = 0; i < 5; i++)
                for (int j = 0; j < 5; j++)
                    currentCube[i][j] = nextCube[i][j];
            createCube(nextCube);
        }
        return true;
    }
    
  
    
/*    public static void main(String[] args) throws Exception {
        GameCore gameCore=new GameCore();
        
        while(true){
            if(!gameCore.startGame())
                break;
            int[][] cube=gameCore.getContainer();
            for(int i=0;i<35;i++){
                for(int j=0;j<20;j++){
                    System.out.print(cube[i][j]);
                }
                System.out.println();
            }
            System.out.println();
        }
    }*/
}
