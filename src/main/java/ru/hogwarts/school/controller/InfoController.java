package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.util.SumTask;

import java.util.concurrent.ForkJoinPool;

@RestController
@RequestMapping("/info")
public class InfoController {

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/port")
    public ResponseEntity<String> getServerPort() {
        return ResponseEntity.ok("The application is running on port: " + serverPort);
    }
    
    @GetMapping("/sum")
    public ResponseEntity<Integer> getSum() {
        int n = 1_000_000;
        int sum = n * (n + 1) / 2;  // Optimized computation using arithmetic series formula
        return ResponseEntity.ok(sum);
    }
    
    @GetMapping("/sum-parallel")
    public ResponseEntity<Integer> getSumUsingForkJoin() {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        int sum = forkJoinPool.invoke(new SumTask(1, 1_000_000));
        return ResponseEntity.ok(sum);
    }
}
