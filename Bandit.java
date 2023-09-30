/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bandit;
import java.util.*;
import java.util.Scanner;
/**
 *
 * @author user
 */
public class Bandit {

    /**
     * @param args the command line arguments
     */
    static double decay=11;
    static double currentmean1=0;
    static double currentmean2=0;
    static int count1=0,count2=0;
    static int explorationcount=0,exploitationcount=0;
    static int arm1underexploration=0,arm1underexploitation=0;
    static int arm2underexploration=0,arm2underexploitation=0;
    static double value1=0,value2=0;
    static int p=0,q=0,explorearm;
    static double temparm1=0,temparm2=0;
    static double reward1[]=new double[1000];
    static double reward2[]=new double[1000];
    static double tfidf[]=new double[] 
  {0.14603521,  0.18336703,  0.13056221,  0.11088843,  0.10957485,
   0.12496269,  0.16049019,  0.2282845,   0.16072935,  0.14635859,  0.13118703,
   0.14407158,  0.10964694,  0.17849428,  0.15741546,  0.17486725,  0.14475398,
   0.17736205,  0.17591172,  0.24415922,  0.12881367,  0.23469961,  0.09160839,
   0.16577778,  0.13636283,  0.17282036,  0.18503174,  0.11177467,  0.16756579,
   0.09917775,  0.12227983,  0.2555446,   0.29115476,  0.14030543,  0.17416155,
   0.18427873,  0.16096979,  0.1395005,   0.16868187,  0.12340496,  0.21193091,
   0.21520273,  0.18144743,  0.11706113,  0.15944666,  0.17513172,  0.20429423,
   0.18189323,  0.20562736,  0.21312507,  0.20350443,  0.15552545,  0.2101857,
   0.14108597,  0.20266022,  0.22354232,  0.15409534,  0.18619986,  0.2192408,
   0.25710281,  0.19391921,  0.17825936,  0.19540999,  0.14608339,  0.17600534,
   0.19349787,  0.18642896,  0.11344614,  0.24547398,  0.31523022,  0.14276961,
   0.27712766,  0.17897889,  0.11979468,  0.17248383,  0.1576059,   0.0692235,
   0.12541631,  0.1538641,   0.19088903,  0.17464017,  0.19521375,  0.13354439,
   0.16897886,  0.22873506,  0.1771753,   0.20208898,  0.21346162,  0.18508256,
   0.20765879,  0.16651365,  0.15285714,  0.29032631,  0.26580692,  0.34862653,
   0.15730934,  0.16759464,  0.19403156,  0.24519186,  0.17747744};
    static double minHash[]=new double[] 
    {0.120448179272,0.120448179272,0.108033240997,0.12676056338,0.120448179272,
    0.0752688172043,0.133144475921,0.129943502825,0.104972375691,0.078167115903,
    0.0928961748634,0.0989010989011,0.133144475921,0.146131805158,0.139601139601,
    0.0928961748634,0.0958904109589,0.0928961748634,0.120448179272,0.159420289855,
    0.0958904109589,0.0752688172043,0.0869565217391,0.0869565217391,0.0958904109589,
0.0958904109589,0.0582010582011,0.117318435754,0.117318435754,0.0958904109589,0.108033240997,
0.133144475921,0.0810810810811,0.149425287356,0.136363636364,0.111111111111,0.114206128134,0.169590643275,
0.101928374656,0.078167115903,0.0840108401084,0.129943502825,0.159420289855,0.123595505618,0.173020527859,
0.133144475921,0.156069364162,0.136363636364,0.169590643275,0.152737752161,0.173020527859,0.123595505618,
0.12676056338,0.123595505618,0.169590643275,0.162790697674,0.0810810810811,0.108033240997,0.0810810810811,
0.101928374656,0.149425287356,0.149425287356,0.0869565217391,0.0610079575597,0.133144475921,0.0869565217391,
0.0810810810811,0.0899182561308,0.0610079575597,0.123595505618,0.142857142857,0.117318435754,0.0869565217391,
0.114206128134,0.120448179272,0.204819277108,0.063829787234,0.104972375691,0.12676056338,0.0840108401084,
0.0869565217391,0.114206128134,0.176470588235,0.133144475921,0.133144475921,0.123595505618,0.156069364162,
0.146131805158,0.146131805158,0.108033240997,0.17994100295,0.136363636364,0.129943502825,0.0958904109589,
0.156069364162,0.133144475921,0.12676056338,0.152737752161,0.17994100295,0.129943502825,0.139601139601,0.159420289855
};
    public double getepsilon(int iteration)
    {
        return ((double) decay)/(iteration + (double) decay);
    }
    public static int comparemean()
    {
        if(currentmean1>currentmean2)
        {
            return 1;
        }
        else 
        {
            return 2;
        }
    }
    public static double tfidf(int k)
    {
      return (double) Math.round(tfidf[k] * 1000.0) / 1000.0;
    }
    public static double minHash(int k)
    {
    return (double) Math.round(minHash[k] * 1000.0) / 1000.0;
    }
    public static void main(String[] args) {
        Random rand=new Random();
        Scanner in=new Scanner(System.in);
        int n;
        System.out.println("How many simulations would you like to run?");
        n=in.nextInt();
        for(int i=1;i<=n;i++)
        {
        System.out.println("Running experiment "+i+"...");
        if(0.0+rand.nextDouble()*1.0>b.getepsilon(i))
        {
        int bestarm=comparemean();
        if(bestarm==1)
        {
        reward1[++p]=tfidf(i-1);
        count1++;
        for(int j=1;j<=p;j++)
        {
        value1+=reward1[p];
        }
        value1=value1/p;
        currentmean1=((count1-1)/ (double) n) * value1 + (1/(double) count1)*reward1[p];
        exploitationcount++;
        arm1underexploitation++;
        System.out.println("Arm 1 pulled(exploitation). Reward collected " + (double) Math.round(reward1[p] * 1000.0) / 1000.0 + ", Current mean of Arm 1 is: " + (double) Math.round(currentmean1 * 1000.0) / 1000.0);
        }
        else
        {
        reward2[++q]=minHash(i-1);
        count2++;
        for(int j=1;j<=q;j++)
        {
        value2+=reward2[q];
        }
        value2=value2/q;
        currentmean2=((count2-1)/ (double) n) * value2 + (1/(double) count2)*reward2[q];
        exploitationcount++;
        arm2underexploitation++;
        System.out.println("Arm 2 pulled(exploitation). Reward collected " + (double) Math.round(reward2[q] * 1000.0) / 1000.0 + ", Current mean of Arm 2 is: " + (double) Math.round(currentmean2 * 1000.0) / 1000.0);
        }
        }
        else
        {
        temparm1=tfidf(i-1);
        temparm2=minHash(i-1);
        if(temparm1>temparm2)
        {
        explorearm=1;
        }
        else
        {
        explorearm=2;
        }
        if(explorearm==1)
        {
        reward1[++p]=tfidf(i-1);
        count1++;
        for(int j=1;j<=p;j++)
        {
        value1+=reward1[p];
        }
        value1=value1/p;
        currentmean1=((count1-1)/ (double) n) * value1 + (1/(double) count1)*reward1[p];
        explorationcount++;
        arm1underexploration++;
        System.out.println("Arm 1 pulled(exploration). Reward collected " + (double) Math.round(reward1[p] * 1000.0) / 1000.0 + ", Current mean of Arm 1 is: " + (double) Math.round(currentmean1 * 1000.0) / 1000.0);
        }
        else if(explorearm==2)
        {
        reward2[++q]=minHash(i-1);
        count2++;
        for(int j=1;j<=q;j++)
        {
        value2+=reward2[q];
        }
        value2=value2/q;
        currentmean2=((count2-1)/ (double) n) * value2 + (1/(double) count2)*reward2[q];
        explorationcount++;
        arm2underexploration++;
        System.out.println("Arm 2 pulled(exploration). Reward collected " + (double) Math.round(reward2[q] * 1000.0) / 1000.0 + ", Current mean of Arm 2 is:" + (double) Math.round(currentmean2 * 1000.0) / 1000.0);
        }
        }
        }
        System.out.println("\t\t\tArm 1 pulled " + count1 + " times with total mean " + (double) Math.round(currentmean1 * 1000.0) / 1000.0 + " & Arm 2 pulled "+ count2 + " times with total mean "+ (double) Math.round(currentmean2 * 1000.0) / 1000.0);
        System.out.println("\t\t\tTotal explorations are: "+ explorationcount + " and total exploitations are: " + exploitationcount);
        System.out.println("\t\t\tArm 1 under exploration: " + arm1underexploration + " and Arm 2 under exploration: " + arm2underexploration);
        System.out.println("\t\t\tArm 1 under exploitation: " + arm1underexploitation + " and Arm 2 under exploitation: " + arm2underexploitation);
    }
    
}
