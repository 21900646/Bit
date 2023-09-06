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
-> 결과  변수를 어떻게 동기화할 지, 몇 개의 스레드를 사용해야할 지 등 생각할 필요 X. <br><br><br>
<br>

### 1-1. 순차 스트림을 병렬 스트림으로 변환하기
```java
public static long parallelSum(long n) {
    return Stream.iterate(1L, i -> i + 1)
                 .limit(n)
                 .parallel()  //  스트림을 병렬 스트림으로 변환
                 .reduce(0L, Long::sum);
}
```
![Uploading image.png…]()



### 1-2. 스트림 성능 측정

### 1-3. 병렬 스트림의 올바른 사용법


## 2. 포크/조인 프레임워크

### 2-1. Recursive Task 활용


### 2-2. 포크/조인 프레임워크를 제대로 사용하는 방법

### 2-3. 작업 훔치기


## 3. Spliterator 인터페이스

### 3-1. 분할 과정

### 3-2. 커스텀 Spliterator 구현하기

