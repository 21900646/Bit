CHAPTER 3. 람다 표현식

1. 람다란 무엇인가?
: 메서드로 전달할 수 있는 '익명 함수를 단순화'한 것. 이름 X. 

* 람다의 특징
  1. 익명 -> 구현해야할 코드 줆.
  2. 함수
  3. 전달
  4. 간결성 (=최대 장점)

* 람다 표현식
      [람다 파라미터]  [화살표]            [람다 바디]
    (Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight());  

  -> 패턴
  (parameters) -> expression
  (parameters) -> { statements; }


---  
2. 어디에, 어떻게 람다를 사용하는가?
2-1. 함수형 인터페이스
: '하나의 추상 메서드'를 갖는 인터페이스. 상속X. 디폴트 메서드 개수는 상관X.
-> 전체 표현식을 함수형 인터페이스의 인스턴스로 취급.
```
Runnable r1 = () -> System.out.println("Hello World 1");     # 람다 사용

Runnable r2 = () -> new Runnable(){                          # 익명 클래스 사용  
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
# 한 개의 void 메소드 호출은 중괄호 필요 X. 

```

2-2. 함수 디스크립터
: 람다 표현식의 시그니처를 서술하는 메서드.
= 어떤 입력값을 받고 어떤 반환값을 주는지에 대한 설명을 람다 표현식 문법으로 표현한 것.
```Java
execute(() -> {});
public void execute(Runnable r){
  r.run();
}

//() -> {} 의 시그니처는 "() -> void"

```
*** functionallInterface란 무엇인가?
: 함수형 인터페이스임을 가르키는 어노테이션.
만약 함수형 인터페이스가 아니라면 에러 발생.
(Mutliple nonoverriding abstract methods found in interface Foo)
```Java
public String procesesFile() throws IOException{
  try(BufferedReader br = new BufferedReader(new FileReader("data.txt"))){
    return br.readLine();
  }
}
```
  
---
3. 실행 어라운드 패턴
: 실제 자원을 처리하는 코드를 설정과 정리 두 과정으로.
즉, 하나의 로직을 수행할때 첫번째로 초기화/준비 코드가 수행되고 마지막에 정리/마무리 코드가 실행된다. 그리고 그 사이에 실제 자원을 처리하는 코드를 실행하는 것이다.

* 실행 어라운드 패턴을 적용하는 4과정
1단계, 동작파라미터화 시키기.
```
String result = processFile(BufferReader br) -> br.readLine() + br.readLine());
```

2단계, 함수형 인터페이스를 이용해서 동작 전달.
```
@FunctionalInterface
public interface BufferedReaderProcessor{
  String process(BufferedReader b) throws IOException;
}

public String processFile(BufferedReaderProcessor p) throws IOException{
 ...
}
```

3단계, 동작 실행
```Java
public String processFile(BufferedReaderProcessor p) throws IOEcpetion{
  try(BufferedReader br = new BufferedReader(new FileReader("data.txt"))){
    return p.process(br);
  }
}
```

4단계, 람다 전달
```Java
String oneLine = processFile((BufferedReader br) -> br.readLine());

String twoLines = processFile((BufferedReader br) -> br.readLine() + br.readLine());
```

---
4. 함수형 인터페이스, 형식 추론
4-1. Predicate
java.util.function.Predicate<T> 인터페이스
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

4-2. Consumer
java.util.function.Consumer<T> 인터페이스
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


4-3. Function





6. 메서드 참조
형식 검사, 형식 추론, 제약에서 컴파일러가 람다 표현식의 유효성을 확인하는 방법.


7. 람다 만들기
