package demo;

public class Work {
    public void process(Data data, Integer...numbers)
    {
        for (int n : numbers)
        {
            data.value += n;
        }
    }
}
