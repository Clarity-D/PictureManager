package com.example.picturemanager.tree;

public class Tree<T>
{
    private T data;
    private String wholeName;
    private int numOfChildren=0;
    private Tree parents;
    private Tree children[];
    private boolean isPictureExist=false;
    private double allSize=0;


    public Tree(T data)//String wholeName
    {
        this.data=data;//this.wholeName=wholeName;
    }
    public double getAllSize()
    {
        return allSize;
    }

    public void setAllSize(double allSize)
    {
        this.allSize=allSize;
    }

    public T GetData()
    {
        return data;
    }

    public void SetData(T data)
    {
        this.data=data;
    }

    public String GetWholeName()
    {
        return wholeName;
    }

    public void setWholeName(String wholeName)
    {
        this.wholeName=wholeName;
    }

    public Tree GetParents()
    {
        return parents;
    }

    public Tree[] GetChildren()
    {
        return children;
    }

    public void SetParents(Tree parents)
    {
        this.parents=parents;
    }

    public void SetChildren(Tree[] children)
    {
        this.children=children;
    }

    public void AddChildren(Tree child)
    {
        if(this.children==null)//优化?
        {
            this.children=new Tree[10000];
        }
        this.children[this.numOfChildren]=child;
    }

    public int GetNumOfChildren()
    {
        return numOfChildren;
    }

    public void SetNumOfChildren(int num)
    {
        this.numOfChildren=num;
    }

    public void AddNumOfChildren()
    {
        this.numOfChildren=this.numOfChildren+1;
    }

    public boolean GetIsPictureExist()
    {
        return isPictureExist;
    }

    public void TrueIsPictureExist()
    {
        this.isPictureExist=true;
    }
}
