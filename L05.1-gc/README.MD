# Измерение активности GC

## Запуск

```
mvn clean package
./start.sh
```

## Результаты

### G1 (GarbageFirst) -XX:+UseG1GC
Total time: 292,883s

| Name | Runs | Duration (min.) |
| --- | --- | --- |
| G1 Young Generation | 14 | 0.0031500063 |
| G1 Old Generation | 2 | 0.0039833413 |

### SerialGC -XX:+UseSerialGC
Total time: 301,184s

| Name | Runs | Duration (min.) |
| --- | --- | --- |
| Copy | 7 | 0.0017833369 |
| MarkSweepCompact | 4 | 0.0066833467 |

### ParallelGC -XX:+UseParallelGC
Total time: 275,770s

| Name | Runs | Duration (min.) |
| --- | --- | --- |
| PS MarkSweep | 5 | 0.012366691400000001 |
| PS Scavenge | 4 | 0.0019666706 |

