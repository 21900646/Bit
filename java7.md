# Chapter 7. 병렬 데이터 처리와 성능
## 1. 병렬 스트림
: 각각의 스레드에서 처리할 수 있도록 스트림 요소를 여러 청크로 분할한 스트림. <br>
- 만드는 방법 : 컬렉션에서 parallelStream을 호출. <br>
- 모든 멀티코어 프로세서가 각각의 청크를 처리하도록 할당 가능. <br><br>

[장점]
기존 소스 :
```java
public long iterativeSum(long n){
  long result = 0;
  for (long i = 1L; i <= n; i++){
    result += i;
  }
  return result;
}
```
<br>
일반 Stream 활용 :

```java
public long sequentialSum(long n){
  return Stream.iterate(1L, i -> i+1)
              .limit(n)
              .parallel()
              .reduce(0L, Long::sum);
```

-> 결과  변수를 어떻게 동기화할 지, 몇 개의 스레드를 사용해야할 지 등 생각할 필요를 없애려면? <br><br>

### 1-1. 순차 스트림을 병렬 스트림으로 변환하기
```java
public static long parallelSum(long n) {
    return Stream.iterate(1L, i -> i + 1)
                 .limit(n)
                 .parallel()  //  스트림을 병렬 스트림으로 변환
                 .reduce(0L, Long::sum);
}
```
![image](https://github.com/21900646/Bit/assets/69943167/e8722154-a828-4570-b163-206ac1057e2d)

하지만, 호출해도 스트림 자체에 아무 변화 X. <br>
이후 연산을 병렬로 수행해야함을 의미하는 불리언 플래그가 설정. <br>
반대는 sequential임. (병렬 스트림 -> 순차 스트림) <br><br><br>



### 1-2. 스트림 성능 측정
: JMH(Java Microbenchmark Harness) 라이브러리를 이용해 성능 측정. <br>
-> 반복, 순차, 병렬 중 무엇이 가장 빠를까? <br><br>

반복 - 순차 - 병렬 순으로 빠름. <br><br><br>

[이유] <br>
- 반복 결과로 박싱된 객체를 생성하므로 이를 다시 언박싱하는 과정이 필요했다. <br>
- 반복 작업은 병렬로 실행될 수 있도록 독립적인 청크로 분할하기 어렵다. <br><br><br>
![image](https://github.com/21900646/Bit/assets/69943167/2dc7499b-961c-4605-9d97-c5d2259e0266)

- 리듀싱 과정을 시작하는 시점에 전체 숫자 리스트가 준비되지 않았으므로 스트림을 병렬로 처리할 수 있도록 청크로 분할할 수가 없기 때문이다.
- 위와 같은 상황에서는 병렬 리듀싱 연산이 수행되지 않는다. <br><br><br>



### 1-3. 병렬 스트림의 올바른 사용법
[문제] 공유된 상태를 바꾸는 알고리즘을 사용함. <br><br>
순차 : 
```java
public long sideEffectParalleSum(long n) {
    Accumulator accumulator = new Accumulator();
    LongStream.rangeClosed(1, n).parallel().forEach(accumulator::add);
    return accumulator.total;
}

public class Accumulator {
    public long total = 0;
    public void add(long value) { total += value; }
}
```
<br>
위의 코드를 병렬로 바꾼다면? :

```java
public long sideEffectParallelSum(long n){
  Accumulator accumulator = new Accumulator();
  LongStream.rangeClosed(1, n).parallel().forEach(accumulator::add);
  return accumulator.total;
```
-> 잘못된 결과 발생. + 레이스 컨디션 발생. <br><br>
** 레이스 컨디션 : 두 개 이상의 프로세스가 발생할 경우, 공유 자원 접근 순서에 따라 결과값이 달라짐.
<br><br><br>

**[병렬 스트림을 효과적으로 사용하는 방법]**
- 확신이 서지 않을 때는 직접 측정해서 사용하라.<br>
(순자 스트림 vs 병렬 스트림 => 벤치마크로 직접 성능 평가) <br><br>

- 박싱을 주의하라. <br>
(자동 박싱과 언박싱은 성능을 저하시키기 때문에 IntStream, LongStream, DoubleStream을 사용하라.) <br><br>
 
- 순차 스트림보다 병렬 스트림에서 성능이 떨어지는 연산이 있다. <br>
(특히 limit이나 findFirst처럼 요소 순서에 의존하는 연산은 비싼 비용을 치뤄야 함.) <br><br>

- 스트림에서 수행하는 전체 파이프라인 연산 비용을 고려하라. (처리할 요소 수 N * 하나의 요소를 처리하는 비용 Q) <br><br>

- 소량의 데이터에서 병렬 스트림은 도움 X. <br>
  (ArrayList를 LinkedList보다 효율적으로 분할할 수 O)<br><br>
  
- 스트림을 구성하는 자료구조가 적절한지 확인하라. <br><br>

- 스트림의 특정과 파이프라인의 중간 연산이 스트림의 특성을 어떻게 바꾸는지에 따라 과정의 성능이 달라진다. <br><br>

- 최종 연산의 병합 과정(예를 들면 combiner 메서드) 비용을 살펴봐라.
<br><br><br>



## 2. 포크/조인 프레임워크
병렬스트림 내부에서 포크/조인 프레임워크를 활용하고 있음. <br>

### 2-1. Recursive Task 활용
- 스레드 풀을 사용하기 위해선, RecursiveTask<R>의 서브 클래스를 구현하면 됨. (R은 RecursiveAction 형식) <br>
- RecursiveTask를 정의하기 위해선, 추상 메서드 compute를 구현해야 함. <br>
```java
protected absract R compute();
```
-> 태스크를 서브태스크로 분할하는 로직과 더 이상 분할할 수 없을 때 개별 서브테스크의 결과를 생산할 알고리즘을 정의한다. <br><br>

** Recursive Action과 Recursive Task 차이 <br>
- Recursive Action : 재귀적 결과 X.
- Recursive Task : 재귀적 결과 O.
<br><br>

정복 알고리즘의 병렬화 버전 : 
```
if(Task is small) { // 테스크가 작아 분할이 불가능
    // 순차적으로 테스크 계산
} else {
    // 테스크를 두 서브 테스크로 분할.
    // 테스크가 다시 서브테스크로 분할되도록 이 메서드를 재귀적으로 호출함.
    // 모든 서브테스크의 연산이 완료될 때까지 기다림.
    // 모든 서브 테스크의 연산이 완료될 때까지 기다리고, 결과를 합침.
}
```
![image](https://github.com/21900646/Bit/assets/69943167/714c64b5-9303-4910-93a9-b1db77da9c63)
<br>** compute()는 작업을 나누고, fork()는 작업을 큐에 넣는다.

### 2-2. 포크/조인 프레임워크를 제대로 사용하는 방법
- 두 서브태스크가 모두 시작된 다음 join을 호출해야 함.
- RecursiveTask 내에는 ForkJoinPool읭 invoke 메서드를 사용 X. (compute나 fork 메서드 직접 호출) <br>
- 서브태스크에 fork 메서드를 호출해서 ForkJoinPool의 일정을 조절할 수 O.
- 포크조인 프레임워크를 이용하는 병렬 계산은 디버깅하기 어렵다.
<br><br>

### 2-3. 작업 훔치기
[배경] 각각의 서브태스크의 작업 완료 시간이 다름. <br><br>
: 할 일이 없어진 스레드가 다른 스레드 큐의 꼬리에서 작업을 훔쳐옴. (이중 연결 리스트를 참조)
![image](https://github.com/21900646/Bit/assets/69943167/88ae688b-c834-42f3-b216-c2f86096e011)


## 3. Spliterator 인터페이스


### 3-1. 분할 과정

### 3-2. 커스텀 Spliterator 구현하기

