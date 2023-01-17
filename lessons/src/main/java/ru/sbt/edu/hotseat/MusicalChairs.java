package ru.sbt.edu.hotseat;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MusicalChairs {
    private static Player WAS_HERE;
    private static boolean STOPPED;

    public static void main(String[] args) throws InterruptedException {
        int moves = multicoreGame();
        System.out.println();
        sequentialGameSimulation(moves / 2);
    }

    private static void sequentialGameSimulation(int rounds) {
        Chair chair = new Chair();
        List<Player> players = Stream.of(Player.values()).collect(Collectors.toList());
        Random random = new Random();

        for (int i = 0; i < rounds; i++) {
            Collections.shuffle(players, random);
            for (Player player : players) {
                boolean won = chair.attemptWin(player);
                if (!won) {
                    chair.reset();
                }
            }
        }

        printStats(chair);
    }

    private static int multicoreGame() throws InterruptedException {
        Chair chair = new Chair();
        Lock lock = new ReentrantLock(true);

        Thread aliceMove = new Thread(playersRunnable(chair, Player.ALICE, lock));
        Thread bobMove = new Thread(playersRunnable(chair, Player.BOB, lock));
        aliceMove.start();
        bobMove.start();

        Thread.sleep(10000);
        STOPPED = true;

        //printLog(chair)
        printStats(chair);
        return chair.log.size();
    }

    private static void printStats(Chair hotChair) {
        //can be replaced with WinRate statistics computation
        List<String> log = hotChair.getLog();
        int rounds = log.size() / 2;
        System.out.println("Total rounds:" + rounds);
        Map<String, Double> winRates = log.stream()
                .limit(rounds * 2)
                .filter(s -> s.endsWith("WON"))
                .collect(Collectors.groupingBy(s -> s.split(" ")[0],
                        Collectors.collectingAndThen(Collectors.counting(), c -> c / (double) rounds)));
        System.out.println(winRates);
    }

    private static void printLog(Chair hotChair) {
        //can be replaced with WinRate statistics computation
        List<String> log = hotChair.getLog();
        for (int i = 0; i < log.size(); i++) {
            System.out.println(log.get(i));
            if (i%2 == 1) {
                System.out.println();
            }
        }
    }

    private static Runnable playersRunnable(Chair hotChair, Player player, Lock lock) {
        return () -> {
            while (!STOPPED) {
                lock.lock();
                try {
                    if (WAS_HERE != player) {
                        boolean won = hotChair.attemptWin(player);
                        if (!won) {
                            hotChair.reset();
                            WAS_HERE = null;
                        } else {
                            WAS_HERE = player;
                        }
                    }
                } finally {
                    lock.unlock();
                }
            }
        };
    }

    public static class Chair {
        private Player hotSeat;

        private final List<String> log = new ArrayList<>();

        public boolean attemptWin(Player player) {
            return hotSeat == null ? win(player) : lose(player);
        }

        private boolean win(Player player) {
            hotSeat = player;
            log.add(player + " WON");
            return true;
        }

        private boolean lose(Player player) {
            log.add(player + " LOST");
            return false;
        }

        public void reset() {
            hotSeat = null;
        }
        public List<String> getLog() {
            return log;
        }

    }
    public enum Player {
        ALICE, BOB
    }
}
