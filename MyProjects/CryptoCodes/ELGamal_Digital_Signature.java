import java.util.Scanner;
//ElGamal Digital Signature
public class ELGamal_Digital_Signature {

    public static int gcd(int a, int b) {
        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }

    public static int squareAndMultiply(int base, int exponent, int modulus) {

        int x=base,y=base;
        String binaryExponent=Integer.toBinaryString(exponent);
        for(int i=1;i<binaryExponent.length();i++){
            if(binaryExponent.charAt(i)=='1'){
                y=(y*x*y)%modulus;
            }else{
                y=(y*y)%modulus;
            }
        }
        int result=y;


        return result;
    }
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

    public static int getPrimtiveElement(int p){
        int result=0;
        for(int i=2;i<p;i++){
            int count=0;
            for(int j=1;j<p-1;j++){
                if(squareAndMultiply(i,j,p)!=1){
                    count++;
                }
            }
            if(count==p-2){
                result=i;
                break;
            }
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
    public static int privateKey=0;

    public static int[] BobKeys(int p, int alpha)
    {   int [] keys = new int[3];
        keys[0]=p;
        keys[1]=alpha;
        privateKey = (int) (Math.random() * (p-2) + 2);
        int publicKey = squareAndMultiply(alpha, privateKey, p);
        keys[2]=publicKey;
        return keys;
    }

    public static int[] SignatureGeneration(int message,int[] BobKeys, int privateKey)
    {
        int p = BobKeys[0];
        int alpha = BobKeys[1];
        int bobPublicKey = BobKeys[2];
        int[] result = new int[3];
        result[0]= message;
        int K_e = (int) (Math.random() * (p-2) + 2);
        while (gcd(K_e, p-1) != 1)
        {
            K_e = (int) (Math.random() * (p-2) + 2);
        }
        int K_e_inverse = ExtendedEuclidianAlgorithm(K_e, p-1);
        int r = squareAndMultiply(alpha, K_e, p);
        if(r < 0)
        {
            r += p;
        }
        result[1] = r;
        int s = (K_e_inverse * (message - privateKey * r)) % (p-1);
        if (s < 0)
        {
            s += p-1;
        }
        result[2] = s;


        return result;
    }
    public static boolean Verification(int[] signature, int[] ChannelValues){
        int p = ChannelValues[0];
        int alpha = ChannelValues[1];
        int r = signature[1];
        int s = signature[2];
        int message = signature[0];
        int BobPublicKey = ChannelValues[2];

        int t1= squareAndMultiply(alpha, message, p);
        System.out.println("t1: " + t1);
        int t2= (squareAndMultiply(BobPublicKey, r, p) * squareAndMultiply(r, s, p)) % p;
        System.out.println("t2: " + t2);
        if(t1==t2){
            return true;
        }else{
            return false;
        }

    }





    public static void main(String[] args) {

        String message;
        System.out.println("Enter 'exit' to end ");
        while(true){
            System.out.println("Enter Message: ");
            Scanner sc = new Scanner(System.in);
            message = sc.nextLine();
            if(message.equals("exit")){
                break;
            }
            while (!message.matches("[0-9]+")) {
                System.out.println("Invalid input, please enter a number: ");
                message = sc.nextLine();
            }
            int messageInt = Integer.parseInt(message);
            int p = (int) (Math.random() * (1000 - 100) + 100);
            while (checkIfPrime(p) == 0) {
                p = (int) (Math.random() * (1000 - 100) + 100);
            }

            System.out.println("Generated Prime Number =  " + p);
            int alpha = getPrimtiveElement(p);
            System.out.println("Primitive Element =  " + alpha);
            int[] BobKeys = BobKeys(p, alpha);
            int[] signature = SignatureGeneration(messageInt, BobKeys, privateKey);
            System.out.println("Message: " + signature[0]);
            System.out.println("r: " + signature[1]);
            System.out.println("s: " + signature[2]);
            boolean verified = Verification(signature, BobKeys);
            if (verified) {
                System.out.println("Signature Verified");
            } else {
                System.out.println("Signature Not Verified");
            }
        }


   }
}