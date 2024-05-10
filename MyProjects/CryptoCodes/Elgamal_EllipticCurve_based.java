//ELGAMAL ELIPTIC CURVE CRYPTOGRAPHY
//20215002 - AHMED HOSSAM
//20216138 - KAREEM BAKRY
//ElGamal Elliptic Curve Cryptography
import java.util.Scanner;

class point {
    int x;
    int y;

    public point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void display(){
        System.out.println("("+x+","+y+")");
    }
}
class Credentials{
    int privateKey;
    point publicKey;

    public Credentials(){
        privateKey=0;
        publicKey=new point(0,0);
    }
    public Credentials(int privateKey, point publicKey){
        this.privateKey=privateKey;
        this.publicKey=publicKey;
    }
    public int getPrivateKey(){
        return privateKey;
    }
    public point getPublicKey(){
        return publicKey;
    }
    public void setPrivateKey(int privateKey){
        this.privateKey=privateKey;
    }
    public void setPublicKey(point publicKey){
        this.publicKey=publicKey;
    }
    public point getPublicofOther(Credentials user){
        return user.getPublicKey();
    }
    public void display(){
        System.out.println("Private Key: "+privateKey);
        System.out.print("Public Key: ");
        publicKey.display();
    }
}
class ChannelKeys{
    point EphimeralKey;
    int CipherText;
    public ChannelKeys(){
        EphimeralKey=new point(0,0);
        CipherText=0;
    }
    public ChannelKeys(point EphimeralKey, int CipherText){
        this.EphimeralKey=EphimeralKey;
        this.CipherText=CipherText;
    }
    public void display(){
        System.out.print("Ephimeral Key: ");
        EphimeralKey.display();
        System.out.println("Cipher Text: "+CipherText);
    }
}
public class Elgamal_EllipticCurve_based {
    public static int ExtendedEuclidianAlgorithm(int a, int b){
        int remainder = b,remainder2=a,s=0,old_s=1,t=1,old_t=0;
        while(remainder2!=0){
            int temp=remainder;
            int q=remainder/remainder2;
            remainder=remainder2;
            remainder2=temp%remainder2;
            temp=s;
            s=old_s-q*s;
            old_s=temp;
            temp=t;
            t=old_t-q*t;
            old_t=temp;
        }
        int result=old_t;
        if(result<0){
            result+=b;
        }
        return result;
    }
    public static int checkIfPrime(int number){
        int result=1;
        for(int i=2;i<number;i++){
            if(number%i==0){
                result=0;
                break;
            }
        }
        return result;
    }
    public static point pointAddition(point p1, point p2, int p, int a){
        int x3=0,y3=0;
        if(p1.x==p2.x && p1.y==p2.y){
            int numerator=(3*p1.x*p1.x+a);
            while (numerator<0){
                numerator+=p;
            }
            int deminominator=(2*p1.y);
            boolean Infinite=false;
            if(deminominator==0){
                Infinite=true;
            }
            while (deminominator<0){
                deminominator+=p;
            }
            int s=(numerator*ExtendedEuclidianAlgorithm(deminominator,p))%p;
            while (s<0){
                s+=p;
            }
            if (Infinite==true){
                x3=0;
                y3=0;}
            else {
                x3 = (s * s - p1.x - p2.x) % p;
                while (x3 < 0) {
                    x3 += p;
                }
                y3 = (s * (p1.x - x3) - p1.y) % p;
                while (y3 < 0) {
                    y3 += p;
                }
            }
        }else{
            int numerator=(p2.y-p1.y);
            while (numerator<0){
                numerator+=p;
            }
            int deminominator=(p2.x-p1.x);
            boolean Infinite=false;
            if(deminominator==0){
                Infinite=true;
            }
            while (deminominator<0){
                deminominator+=p;
            }
            int s=(numerator*ExtendedEuclidianAlgorithm(deminominator,p))%p;
            while(s<0){
                s+=p;
            }
            if (Infinite==true){
                x3=0;
                y3=0;}
            else{
             x3=(s*s-p1.x-p2.x)%p;
             while (x3<0){
                 x3+=p;
             }
             y3=(s*(p1.x-x3)-p1.y)%p;
             while (y3<0){
                 y3+=p;
              }
             }
        }
        return new point(x3,y3);
    }
    public static point pointDoubling(point p1, int p, int a){
        int x3=0,y3=0;
        int numerator=(3*p1.x*p1.x+a);
        while (numerator<0){
            numerator+=p;
        }
        int deminominator=(2*p1.y);
        boolean Infinite=false;
        if(deminominator==0){
            Infinite=true;
        }
        while (deminominator<0){
            deminominator+=p;
        }
        int s=(numerator*ExtendedEuclidianAlgorithm(deminominator,p))%p;
        while (s<0){
            s+=p;
        }
        if (Infinite==true){
            x3=0;
            y3=0;}
        else {
            x3 = (s * s - p1.x - p1.x) % p;
            while (x3 < 0) {
                x3 += p;
            }
            y3 = (s * (p1.x - x3) - p1.y) % p;
            while (y3 < 0) {
                y3 += p;
            }
        }
        return new point(x3,y3);
    }
    public static point DoubleAndAdd(int k, point p, int p1, int a){
        point result=new point(0,0);
        String binary=Integer.toBinaryString(k);
        for(int i=0;i<binary.length();i++){
            if(binary.charAt(i)=='1'){
                if(result.x==0 && result.y==0){
                    result=p;
                }else{
                    result=pointAddition(result,p,p1,a);
                }
            }
            p=pointDoubling(p,p1,a);
        }
        return result;
    }
    public static int getOrd(int p, int a, point g){
        int ord=1;
        point temp=g;
        Boolean Cond=true;
        while(Cond){
            temp=pointAddition(g,temp,p,a);
            ord++;
            if(temp.x==0 && temp.y==0 || ord>p+2*Math.sqrt(p)){
                Cond=false;
            }
        }
        return ord;
    }
    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////
    public static void setKeys(Credentials user, int p, int a, point g){
        int Ord=getOrd(p,a,g);
        int kPr=(int)(Math.random()*(Ord-1)+2);
        user.setPrivateKey(kPr);
        user.setPublicKey(DoubleAndAdd(kPr,g,p,a));
    }
    public static ChannelKeys SendMessage(Credentials user1, Credentials user2, int p, int a, point g, int message){
        int Order=getOrd(p,a,g);

        int i=(int)(Math.random()*(Order-2)+2);
        System.out.println("i =  "+i);
        point EphimeralKey=DoubleAndAdd(i,g,p,a);
        point MaskedKey=DoubleAndAdd(i,user2.getPublicKey(),p,a);
        System.out.println("Masked Key at Alice: ");
        MaskedKey.display();
        int CipherText=message+MaskedKey.x;
        return new ChannelKeys(EphimeralKey,CipherText);
    }
    public static int decryptMessage(Credentials user ,ChannelKeys channelKeys, int p, int a){
        point MaskedKey=DoubleAndAdd(user.getPrivateKey(),channelKeys.EphimeralKey,p,a);
        System.out.println("Masked Key at Bob: ");
        MaskedKey.display();
        int message=channelKeys.CipherText-MaskedKey.x;
        return message;

    }



    public static point getPrimitivePoint(int p, int a){
        point result=new point(0,0);
        for(int i=1;i<p;i++){
            for(int j=1;j<p;j++){
                if(getOrd(p,a,new point(i,j))==p-1 || DoubleAndAdd(p-1,new point(i,j),p,a).x==0 && DoubleAndAdd(p-1,new point(i,j),p,a).y==0){  //Condition may not be accuratly correct
                    result=new point(i,j);
                    break;
                }
            }
        }
        return result;
    }



    public static int[] extract_a_b(String equation) {
        String equationWithoutSpaces = equation.replaceAll("\\s", "");

        int index_a = 6;
        int index_b = 9;

        char char_a = equationWithoutSpaces.charAt(index_a);
        char char_b = equationWithoutSpaces.charAt(index_b);

        int a = Character.getNumericValue(char_a);
        int b = Character.getNumericValue(char_b);

        return new int[]{a, b};
    }



    public static void main(String[] args)
    {
        while(true) {
            String message;
            System.out.println("Enter Message: ");
            Scanner sc = new Scanner(System.in);
            message = sc.nextLine();
            while (!message.matches("[0-9]+")) {
                System.out.println("Invalid input, please enter a number: ");
                message = sc.nextLine();
            }
            int messageInt = Integer.parseInt(message);
            //generate random number till it is prime
            int p = (int) (Math.random() * (1000 - 100) + 100);
            while (checkIfPrime(p) == 0) {
                p = (int) (Math.random() * (1000 - 100) + 100);
            }


            System.out.println("Generated Prime Number =  " + p);


            Credentials Alice = new Credentials();
            Credentials Bob = new Credentials();
            String equation = "y2 = x3 + 5x + 6";
            System.out.println("Elliptic Curve Equation: " + equation);
            System.out.println("Please Wait...");
            System.out.println("---------------------------------------------");
            int[] a_b = extract_a_b(equation);
            int a = a_b[0];
            int b = a_b[1];
            point g = getPrimitivePoint(p, a);
            System.out.println("Primitive Point: ");
            g.display();
            setKeys(Alice, p, a, g);
            Alice.display();
            setKeys(Bob, p, a, g);
            Bob.display();
            ChannelKeys channelKeys = SendMessage(Alice, Bob, p, a, g, messageInt);
            channelKeys.display();


            int decryptedMessage = decryptMessage(Bob, channelKeys, p, a);
            System.out.println("Decrypted Message: " + decryptedMessage);
            System.out.println("---------------------------------------------");
            System.out.println("Do you want to continue? (Y/N)");
            String choice = sc.nextLine();
            if (choice.equals("N")) {
                break;
            }
            System.out.println("---------------------------------------------");
        }

        



    }

}
