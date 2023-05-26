//GameReady.java

package Baseball;

import java.util.*;



public class GameReady {
    public int[] ready(){
        int arr[] = new int[4];
        Random r = new Random();

        for(int i=0; i<arr.length; i++){
            arr[i] = r.nextInt(9); // 0 ~ 9까지의 난수
            for(int j=0; j<i; j++){
                if(arr[0] == 0|| arr[i] == arr[j]){
                    i=i-1;
                }
            }
        }

//        // 난수 확인용. 확인 후 삭제 요망.
//        for(int i=0; i<arr.length; i++){
//            System.out.println("난수 " + (i+1) + " : " + arr[i]);
//        }

        return arr;
    }


    public int[] compareCard(int[] userNum, int[] answer) {
        int[] result = new int[2];


        // index, 숫자 동일하면 strike + 1
        for(int i = 0; i < userNum.length; i++) {
            for(int j = 0; j < answer.length; j++) {
                if(userNum[i] == answer[j]) {
                    if (i == j) {
                        result[0]++;
                    } else {
                        result[1]++;
                    }
                }
            }
        }

        // index 는 다르고 숫자만 똑같으면 ball + 1

        return result;
    }

}
