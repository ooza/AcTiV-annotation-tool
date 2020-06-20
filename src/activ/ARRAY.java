package activ;

//package masterwork;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

public class ARRAY
{
  private ARRAY()
  {
  }
  
  public static final java.util.Arrays $ = null;
  
  public static int max(int... data)
  {
    int max = data[0];
    for (int value : data)
      if (value > max) max = value;
    return max;
  }
  
  public static double max(double... data)
  {
    double max = data[0];
    for (double value : data)
      if (value > max) max = value;
    return max;
  }
  
  public static int min(int... data)
  {
    int min = data[0];
    for (int value : data)
      if (value < min) min = value;
    return min;
  }
  
  public static double min(double... data)
  {
    double min = data[0];
    for (double value : data)
      if (value < min) min = value;
    return min;
  }

  public static double[] copy(double... data)
  {
    if(data==null)
      return null;
    double[] copy = new double[data.length];
    System.arraycopy(data,0,copy,0,data.length);
    return copy;
  }
  
  public static double[][] copy(double[][] data)
  {
    if(data==null)
      return null;
    double[][] copy = new double[data.length][];
    for (int y = 0; y < copy.length; y++)
      copy[y] = copy(data[y]);
    return copy;
  }
  
  public static int[] copy(int... data)
  {
    if(data==null)
      return null;
    int[] copy = new int[data.length];
    System.arraycopy(data,0,copy,0,data.length);
    return copy;
  }
  
  public static int[][] copy(int[][] data)
  {
    if(data==null)
      return null;
    int[][] copy = new int[data.length][];
    for (int y = 0; y < copy.length; y++)
      copy[y] = copy(data[y]);
    return copy;
  }
  
  public static int[][] create(int[][] data)
  {
    return new int[data.length][data[0].length];
  }
  
 public static int valfrequente(int []histogramme)
		{int i,j,VF=0,nbre=0;
		 int N=histogramme.length;
		 boolean ok=true;
		i=0;
		while(i< N-1)
		{j=0;
		 if(histogramme[i]>0)
		   { ok=(histogramme[i]==histogramme[i+1]);
			while ((i<N) && ok )
			{i++;
			 j++;
			 if(i<N-1)
			  ok=(histogramme[i]==histogramme[i+1]);
			 else
			  ok=false;
			}
			if(nbre<j)
			{nbre=j;
			 VF=histogramme[i];
			}
		   }
		  i++;
		}
		return VF;
		}
 public static int find(String []tab,String name) throws UnsupportedEncodingException
 {
     int i=0;
     int a;
     boolean trouve=false;
     while((i<tab.length)&&(trouve==false)){
         if((new String(tab[i].getBytes(),"UTF-8")).equals(new String(name.getBytes(),"UTF-8")))
      // if(tab[i].compareTo(name)!=-1)
             trouve=true;
     i++;}
     
     if(trouve==true)
             a= (i-1);
         else 
             a= (-1);
     return a;
 }
 public static int find2(String []tab,String name)
 {
     int i=0;
     int a;
     boolean trouve=false;
    
     while((i<tab.length)&&(trouve==false)){
         if(tab[i].equals(name))
             trouve=true;
     i++;}
     
     if(trouve==true)
             a= (i-1);
         else 
             a= (-1);
     return a;
 }
 public static boolean findB(Vector v,String name)
 {
     int i=0;
     int a;
     boolean trouve=false;
    
     while((i<v.size())&&(trouve==false)){
         if(new String(v.get(i).toString()).equals(name))
             trouve=true;
     i++;}
     
     return trouve;
 }
  public static int findB1(Vector v,String name) throws UnsupportedEncodingException
 {
     int i=0;
     int a;
     boolean trouve=false;
    
     while((i<v.size())&&(trouve==false)){
         if(new String(v.get(i).toString().getBytes(),"UTF-8").equals(name))
             trouve=true;
     i++;}
     if(trouve==true)
             a= (i-1);
         else 
             a= (-1);
     return a;
 }
 public static int find(double []tab,double name)
 {
     int i=0;
     int a;
     boolean trouve=false;
     while((i<tab.length)&&(trouve==false)){
         if(tab[i]==name)
             trouve=true;
     i++;}
     
     if(trouve==true)
             a= (i-1);
         else 
             a= (-1);
     return a;
 }
  public static int findInt(int []tab,int name)
 {
     int i=0;
     int a;
     boolean trouve=false;
     int Comp=0;
     while((i<tab.length)&&(trouve==false)){
         if(tab[i]==name)
             trouve=true;
     i++;}
     
     if(trouve==true)
             a= (i-1);
         else 
             a= (-1);
     return a;
 }
    public static int findNBOcc(int []tab,int name)
 {
     int i=0;
    
    int comp=0;
     while((i<tab.length)){
         if(tab[i]==name)
             comp++;
     i++;}
     
    
     return comp;
 }
  public static boolean findIntB(int []tab,int name)
 {
     int i=0;
     int a;
     boolean trouve=false;
     while((i<tab.length)&&(trouve==false)){
         if(tab[i]==name)
             trouve=true;
     i++;}
     
    
     return trouve;
 }
  public static boolean findString(String tab,String name)throws UnsupportedEncodingException
 {
     int i=0;
   // tab=new String (tab.getBytes(),"UTF-8");
  //  name=new String (name.getBytes(),"UTF-8");
     boolean trouve=false;
   System.out.println(tab.length());
     while((i<tab.length())&&(trouve==false))
     {
         if(tab.charAt(i)==name.charAt(0))
             trouve=true;
     i++;}
     
    
     return trouve;
 }
}
