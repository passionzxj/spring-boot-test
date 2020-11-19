package com.zhang.java8;





import com.zhang.config.exception.BizException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestTransaction {

    private List<Transaction> transactions = null;

    public void before() {
        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario", "Milan");
        Trader alan = new Trader("Alan", "Cambridge");
        Trader brian = new Trader("Btian", "Cambridge");

        transactions = Arrays.asList(
                new Transaction(brian, 2011, 300),
                new Transaction(raoul, 2012, 1000),
                new Transaction(raoul, 2011, 400),
                new Transaction(mario, 2012, 710),
                new Transaction(mario, 2012, 700),
                new Transaction(alan, 2012, 950)
        );

    }
    //1.找出2011年发生的所有交易,并按交易额排序(从低到高)
    @Test
    public void test1() throws Exception{
        before();
        transactions.stream()
                .filter(e->e.getYear()==2011)
                .sorted(Comparator.comparingInt(Transaction::getValue))
                .forEach(System.out::println);
    }
    //2.交易员都在哪些不同的城市工作过?
    @Test
    public void test2() throws Exception{
        before();
        transactions.stream()
                .map(e->e.getTrader().getCity())
                .distinct()
                .forEach(System.out::println);
    }
    //3.查找所有来自剑桥的交易员,并按姓名排序.
    @Test
    public void test3() throws Exception{
        before();
        transactions.stream()
                .filter(e->e.getTrader().getCity().equals("Cambridge"))
                .map(Transaction::getTrader)
                .sorted(Comparator.comparing(Trader::getName))
                .distinct()
                .forEach(System.out::println);
    }
    //4.返回所有交易员的姓名字符串,按字母顺序排序
    @Test
    public void test4() throws Exception{
        before();
        String collect = transactions.stream()
                .map(e -> e.getTrader().getName())
                .sorted()
                .collect(Collectors.joining());
        System.out.println(collect);
        String str = transactions.stream()
                .map(e->e.getTrader().getName())
                .flatMap(TestTransaction::getCharacterStr)
                .sorted(String::compareToIgnoreCase)
                .collect(Collectors.joining());
        System.out.println(str);

    }
    public static Stream<String> getCharacterStr(String string){
        List<String> list = new ArrayList<>();
        for (Character c : string.toCharArray()) {
            list.add(c.toString());
        }
        return list.stream();
    }

    //5.有没有交易员是在米兰工作的?
    @Test
    public void test5() throws Exception{
        before();
        boolean milan = transactions.stream()
                .anyMatch(e -> e.getTrader().getCity().equals("Milan"));
        System.out.println(milan);
    }
    //6.打印生活在剑桥的交易员的所有交易额.
    @Test
    public void test6() throws Exception{
        before();
        IntSummaryStatistics cambridge = transactions.stream()
                .filter(e -> e.getTrader().getCity().equals("Cambridge"))
                .map(Transaction::getValue)
                .collect(Collectors.summarizingInt(Integer::intValue));
        System.out.println(cambridge.getSum());
        Optional<Integer> cambridge1 = transactions.stream()
                .filter(e -> e.getTrader().getCity().equals("Cambridge"))
                .map(Transaction::getValue)
                .reduce(Integer::sum);
        System.out.println(cambridge1.get());
    }
    //7.所有交易中,最高的交易额是多少.
    @Test
    public void test7() throws Exception{
        before();
        Optional<Integer> max = transactions.stream()
                .map(Transaction::getValue)
                .max(Integer::compare);
        System.out.println(max.get());

    }
    //8.找到交易额最小的交易.
    @Test
    public void test8() throws Exception{
        before();
        Optional<Integer> min = transactions.stream()
                .map(Transaction::getValue)
                .min(Integer::compare);
        System.out.println(min.get());
        IntSummaryStatistics collect = transactions.stream()
                .map(Transaction::getValue)
                .collect(Collectors.summarizingInt(Integer::intValue));
        System.out.println(collect.getMin());
    }

    @Test
    public void test9() throws Exception{
        before();
        for (Transaction transaction : transactions) {
            String name = Optional.ofNullable(transaction)
                    .map(Transaction::getTrader)
                    .map(Trader::getName)
//                    .orElseThrow(() -> new BizException("fsdfsfsfs"));
            .orElse("ss");
            System.out.println(name);
        }

        List<String> list = transactions.stream()
                .filter(e -> e.getYear() == 2011)
                .map(Transaction::getTrader)
                .map(s -> Optional.ofNullable(s.getName()).orElse("ss"))
                .collect(Collectors.toList());
        System.out.println(list);
    }

    @Test
    public void test() throws Exception{
        Integer[] ss = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};
        List<Integer> list = Arrays.asList(ss);

        int index = 1;
        int size = 5;
        List<Integer> collect = list.stream().skip((index - 1) * size).limit(size).collect(Collectors.toList());
        for (Integer integer : collect) {
            System.out.println(integer);
        }
    }
    @Test
    public void test111() throws Exception{
        String[] ss = {"你好","hello","份课件反馈","发啊生","忽高忽低","华师大","哈哈哈","华师大1","个个都是","高第后归道","发我"};
        List<String> list = Arrays.asList(ss);

        String str = "发";
        int index = 1;
        int size = 5;
        List<String> collect = list.stream()
                .filter(e -> {
                    if (StringUtils.isNotBlank(str)) {
                        return e.contains(str);
                    }
                    return true;
                })
                .skip((index - 1) * size).limit(size).collect(Collectors.toList());
        for (String integer : collect) {
            System.out.println(integer);
        }
    }
}
