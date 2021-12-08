package main.standard.model;

import main.standard.entities.Roller;

public class Lock {
    private Boolean isLeft;
    private int num;
    public Lock(){
        this.num = 0;
        this.isLeft = false;
    }
    public Boolean isAllow(Roller roller){
        if (num==0){
            return true;
        }else if (num == 1){
            if (roller.isLeft()==this.isLeft){
                return true;
            }
        }
        return false;
    }
    public void access(Roller roller){
        this.num++;
        if (roller.isLeft()){
            this.isLeft = true;
        }else if (roller.isRight()){
            this.isLeft = false;
        }
    }
    public void quit(Roller roller){
        if (this.num>0){
            this.num--;
        }
    }
}
