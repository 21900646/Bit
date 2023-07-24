# CHAPTER 3. 람다 표현식

## 1. 람다란 무엇인가? <br>
: 메서드로 전달할 수 있는 '익명 함수를 단순화'한 것. 이름 X. 



#### * 람다의 특징
  1. 익명 -> 구현해야할 코드 줆.
  2. 함수
  3. 전달
  4. 간결성 (=최대 장점)

<br>

#### * 람다 표현식
       [람다 파라미터]  [화살표]            [람다 바디]
    (Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight());  

<br>
  -----------[패턴]--------------- <br>
  (parameters) -> expression <br>
  (parameters) -> { statements; }
<br><br>

---  
## 2. 어디에, 어떻게 람다를 사용하는가?
### 2-1. 함수형 인터페이스
: '하나의 추상 메서드'를 갖는 인터페이스. 상속X. 디폴트 메서드 개수는 상관X. <br>
확인된 예외를 던지는 동작을 허용X. <br>
- 확인된 예외를 선언하는 함수형 인터페이스를 직접 정의.
- 람다를 try/catch 블록으로 감싼다.
  
-> 전체 표현식을 함수형 인터페이스의 인스턴스로 취급.
```Java
Runnable r1 = () -> System.out.println("Hello World 1");     // 람다 사용

Runnable r2 = () -> new Runnable(){                          // 익명 클래스 사용  
  public void run() {
    System.out.println("Hello World 2");
  }
};

public static void process(Runnable r){
  r.run();
}

process(r1);
process(r2);
process(() -> System.out.println("Hello World 3"));
// 한 개의 void 메소드 호출은 중괄호 필요 X. 
```

### 2-2. 함수 디스크립터
: 람다 표현식의 시그니처를 서술하는 메서드. <br>
= 어떤 입력값을 받고 어떤 반환값을 주는지에 대한 설명을 람다 표현식 문법으로 표현한 것.
```Java
execute(() -> {});
public void execute(Runnable r){
  r.run();
}

//() -> {} 의 시그니처는 "() -> void"
```
** *functionallInterface란 무엇인가?
: 함수형 인터페이스임을 가르키는 어노테이션. <br>
만약 함수형 인터페이스가 아니라면 에러 발생. <br>
(Mutliple nonoverriding abstract methods found in interface Foo)*

```Java
public String procesesFile() throws IOException{
  try(BufferedReader br = new BufferedReader(new FileReader("data.txt"))){
    return br.readLine();
  }
}
```
<br>

---
## 3. 실행 어라운드 패턴
: 실제 자원을 처리하는 코드를 설정과 정리 두 과정으로. <br>
즉, 하나의 로직을 수행할때 첫번째로 초기화/준비 코드가 수행되고 마지막에 정리/마무리 코드가 실행된다. 그리고 그 사이에 실제 자원을 처리하는 코드를 실행하는 것이다.<br>

### 실행 어라운드 패턴을 적용하는 4과정 <br>
#### 1단계, 동작파라미터화 시키기.
```
String result = processFile(BufferReader br) -> br.readLine() + br.readLine());
```
<br>

#### 2단계, 함수형 인터페이스를 이용해서 동작 전달.
```
@FunctionalInterface
public interface BufferedReaderProcessor{
  String process(BufferedReader b) throws IOException;
}

public String processFile(BufferedReaderProcessor p) throws IOException{
 ...
}
```
<br>

#### 3단계, 동작 실행
```Java
public String processFile(BufferedReaderProcessor p) throws IOEcpetion{
  try(BufferedReader br = new BufferedReader(new FileReader("data.txt"))){
    return p.process(br);
  }
}
```
<br>

#### 4단계, 람다 전달
```Java
String oneLine = processFile((BufferedReader br) -> br.readLine());

String twoLines = processFile((BufferedReader br) -> br.readLine() + br.readLine());
```
<br>

---
## 4. 함수형 인터페이스, 형식 추론
* 제네릭 함수형 인터페이스
: 여기엔 참조형만 사용가능.

#### 1) Predicate
java.util.function.Predicate<T> 인터페이스<br>
: test라는 추상메서드를 정의, test는 제네릭 형식 T 객체를 인수로 받음. -> 불리언을 반환.
```Java
@FunctionalInterface
public interface Predicate<T>{
  boolean test(T t);
}

public <T> List<T> filter(List<T> list, Predicate<T> p){
  List<T> results = new ArrayList<>();
  for(T t: list){
    if(p.test(t)){
      results.add(t);
    }
  }
  return results;
}
Predicate<String> nonEmptyStringPredicate = (String s) -> !s.isEmpty();
List<String> nonEmpty = filter(listOfStrings, nonEmptyStringPredicate);
```

#### 2) Consumer
java.util.function.Consumer<T> 인터페이스<br>
: accept라는 추상 메서드 -> 제네릭 형식 T객체를 받아서 void를 반환.
```Java
@FunctionallInterface
public interface Consumer<T> {
  void accept(T t);
}

public <T> void forEach(List<T> list, Consumer<T> c){
  for(T t: list){
    c.accpet(t);
  }
}

forEach(Arrays.asList(1,2,3,4,5), (integer i) -> System.out.println(i));
```


#### 3) Function
java.util.function.Function<T, R> 인터페이스<br>
: 제네릭 형식 T를 인수로 받아서 제네릭 형식 R 객체를 반환하는 추상 메서드 apply를 정의.
```Java
@FunctionalInterface
public interface Function<T, R>{
  R apply(T t);
}

public <T, R> List<R> filter(List<T> list, Predicate<T, R> f){
  List<R> results = new ArrayList<>();
  for(T t: list){
    result.add(f.apply(t));
  }
  return result;
}

list<Integer> l = map(Arrays.asList("lambdas", "in", "actions"), (String s) -> s.length());
```


* 특화된 형식의 함수형 인터페이스
*박싱 : 기본형을 참조형으로 변환하는 기능 (반대는 언박싱, 자동은 오토박싱)*
오토박싱을 피할 수 있도록 하는 IntPredicate 인터페이스
```Java
public interface IntPredicate{
  boolean test(int t);
}

IntPredicate evenNumbers = (int i) -> i % 2 == 0;
evenNumbers.test(1000); # 참(박싱없음)

Predicate<Integer> oddNumbers = (Integer i) -> i % 2 != 0;
oddNumbers.test(1000); # 거짓(박싱)
```

DoublePredicate, IntConsumer, LongBinaryOperator, IntFunction 등등.

```
* 자바8에 추가된 함수형 인터페이스
  [함수형 인터페이스]    [함수 디스크립터]
    Predicate<T>           T -> boolean
    Cunsumer<T>            T -> void
    Function<T, R>         T -> R
    Supplier<T>            () -> T
    UnaryOperator<T>       T -> T
    BinaryOperator<T>      (T, T) -> T
    BiPredicate<L, R>      (T, U) -> boolean
    BiConsumer<T, U>       (T, U) -> void
    BiFunction<T, U, R>    (T, U) -> R
```
  

## 5. 형식 검사, 형식 추론, 제약
#### 1) 형식 검사
: 콘텍스트를 통해 람다의 형식(Type)을 추론 가능

```
1. filter 메서드의 선언 확인하기.
2. filter 메서드는 두번째 파라미터로 Predicate<Apple> 형식(대상 형식)을 기대한다.
3. Predicate<Apple>은 test라는 한 개의 추상 메서드를 정의하는 함수형 인터페이스이다.
4. test 메서드는 Apple을 받아 boolean을 반환하는 함수 디스크립터를 묘사한다.
```


#### 2) 같은 람다, 다른 함수형 인터페이스
```

```


  
#### 3) 형식 추론
: 자바 컴파일러는 람다 표현식이 사용된 콘텍스트를 이용해서 람다 표현식과 관련된 함수형 인터페이스를 추론.
-> 대상형식을 사용하여 함수 디스크립터를 알 수 있으므로, 람다의 시그니처도 추론 O.
```Java
Comparator<Apple> c = (Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight());   # 형식 추론 X.
Compareator<Apple> c = (a1, a2) -> a1.getWeight().compareTo(a2.getWeight());    # 형식 추론.
```


#### 4) 지역 변수 사용
*람다 캡처링 : 람다 표현에서 자유 변수 활용*
제약 사항: 시적으로 final  키워드가 붙거나 final처럼 변경없이 사용해야 함.
```Java
int portNumber = 1337;
Runnable r = () -> System.out.println(portNumber);
// int portNumber = 31337; 이렇게 하면 오류가 난다. (두번 할당했기 때문) 
```
-> 제약 이유 : 인스턴슷 변수는 힙에, 지역 변수는 스택에 위치.
원래 변수에 접근을 허용하는 것이 아니라, 자유 지역 변수의 복사본을 제공. => 한 번만 값을 할당해야 함.

형식 검사, 형식 추론, 제약에서 컴파일러가 람다 표현식의 유효성을 확인하는 방법.




## 6. 메서드 참조
: 특정 메서드만을 호출하는 람다의 축약형.
```Java
inventory.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));
inventory.sort(comparing(Apple::getWeight));        # 메서드 참조 !
```

```
[람다]                                    [메서드 참조 단축 표현]

(Apple apple) -> apple.getWeight()        Apple::getWeight

() -> Thread.currentThread().dumpStack()  Thread.currentThread()::dumpStack

(str, i) -> str.substring(i)              String::substring

(String s) -> System.out.println(s)       System.out::println

(String s) -> this.isValidNames(s)        this::inValidName
```

* 만드는 법

  **1. 정적 메소드 참조**
     ex) Integer의 parseInt 메서드 => Integer::parseInt
  
  **2. 다양한 형식의 인스턴스 메서드 참조**
     ex) String의 length 메서드 => String::length
  
  **3. 기존 객체의 인스턴스 메서드 참조**
     ex) () -> expensiveTransaction.getValue() => expensiveTransaction::getValue
  
    -> 비공개 헬퍼 메서드를 정의한 상황에서 유용.
  
    ```Java
    private boolean isValidName(String string){
      return Character.isUpperCase(string.charAt(0));
    }

    filter(words, this::isValidName)
    ```
* 생성자 참조




## 7. 람다, 메서드 참조 활용하기
1단계, 코드 전달하기
```java
public class AppleComparator implements Comparator<Apple>{
  public int compare(Apple a1, Apple a2){
    return a1.getWeight().compareTo(a2.getWeight());
  }
}
inventory.sort(new AppleComparator());
```

2단계, 익명 클래스 사용
```java
inventory.sort(new Comparator<Apple>(){
  public int compare(Apple a1, Apple a2){
    return a1.getWeight().compareTo(a2.getWeight());
  }
});
```
3단계, 람다 표현식 사용
```java
inventory.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight())
```

4단계, 메서드 참조 사용

```java
inventory.sort(comparing(Apple::getWeight));
```


## 8. 람다 표현식을 조합할 수 있는 유용한 메서드

#### 1) Comparator 조합
```Java
// 역정렬
inventory.sort(comparing(Apple::getWeight).reversed());

// 같은 무게 처리. -> 국가별 정렬로.
inventory.sort(comparing(Apple::getWeight).reversed().thenComparing(Apple::getCountry));
```


#### 2) Predicate 조합.
-> Predicate 인터페이스는 복잡한 프리디케이트를 만들 수 있도록 **negate(결과 반전), and, or** 세가지 메서드 제공.
```Java
Predicate<Apple> notRedApple = redApple.negate();

Predicate<Apple> redAndHeavyApple = redApple.and(apple.getweight() > 150);

Predicate<Apple> redAndHeavyAppleOrGreen =
  redApple.and(apple.getweight() > 150).or(apple -> GREEN.equals(a.getColor()));
```


#### 3) Function 조합.
-> Function 인터페이스는 Function 인스턴스를 반환하는 andThen, compose 두 가지 디폴트 메서드를 제공.
```Java
public class Letter{
  public static String addHeader(String text){
    return "From Raoul, Mario and Alan: " + text;
  }
  public static String addFooter(String text){
    return text + " Kind regards";
  }
  public static String checkSpelling(String text){
    return text.replaceAll("labda", "lamda");
  }
}

Function<String, String> addHeader = Letter::addHeader;
Function<String, String> transformationPipeline = addHeader.andThen(Letter::checkSpelling)
                                                            .andThen(Letter::addFooter);
```
<br><br>
---
### 정리
1. 람다 표현식 : 익명 함수의 일종. 간결한 코드 구현 가능.
2. 함수형 인터페이스 : 하나의 추상 메서드만을 정의하는 인터페이스.
3. 함수형 인터페이스의 추상 메서드를 즉석으로 제공 가능.
4. 람다 표현식 전체가 함수형 인터페이스의 인스턴스로 취급.







