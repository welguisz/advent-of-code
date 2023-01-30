package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AuntSue extends AoCDay {
    public static class Aunt {
        public Integer number;
        public Integer children;
        public Integer cats;
        public Integer samoyeds;
        public Integer pomerians;
        public Integer akitas;
        public Integer vizslas;
        public Integer goldfish;
        public Integer trees;
        public Integer cars;
        public Integer perfumes;

        public Aunt(Integer number, Integer children, Integer cats, Integer samoyeds, Integer pomerians, Integer akitas,
                    Integer vizslas, Integer goldfish, Integer trees, Integer cars, Integer perfumes) {
            this.number = number;
            this.children = children;
            this.cats = cats;
            this.samoyeds = samoyeds;
            this.pomerians = pomerians;
            this.akitas = akitas;
            this.vizslas = vizslas;
            this.goldfish = goldfish;
            this.trees = trees;
            this.cars = cars;
            this.perfumes = perfumes;
        }

        public Integer checkChildren(Integer val) {
            if (children == -1) {
                return 0;
            }
            return (children == val) ? 1 : 0;
        }
        public Integer checkCats(Integer val) {
            if (cats == -1) {
                return 0;
            }
            return (cats == val) ? 1: 0;
        }
        public Integer checkRetroCats(Integer val) {
            if (cats == -1) {
                return 0;
            }
            return (cats > val) ? 1 : 0;
        }
        public Integer checkSamoyeds(Integer val) {
            if (samoyeds == -1) {
                return 0;
            }
            return (samoyeds == val) ? 1 : 0;
        }
        public Integer checkPomerians(Integer val) {
            if (pomerians == -1) {
                return 0;
            }
            return (pomerians == val) ? 1 : 0;
        }
        public Integer checkRetroPomerians(Integer val) {
            if (pomerians == -1) {
                return 0;
            }
            return (pomerians < val) ? 1: 0;
        }
        public Integer checkAkitas(Integer val) {
            if (akitas == -1) {
                return 0;
            }
            return (akitas == val) ? 1 : 0;
        }
        public Integer checkVizslas(Integer val) {
            if (vizslas == -1) {
                return 0;
            }
            return (vizslas == val) ? 1 : 0;
        }
        public Integer checkGoldfish(Integer val) {
            if (goldfish == -1) {
                return 0;
            }
            return (goldfish == val) ? 1 : 0;
        }
        public Integer checkRetroGoldfish(Integer val) {
            if (goldfish == -1) {
                return 0;
            }
            return (goldfish < val) ? 1 : 0;
        }
        public Integer checkTrees(Integer val) {
            if (trees == -1) {
                return 0;
            }
            return (trees == val) ? 1 : 0;
        }
        public Integer checkRetroTrees(Integer val) {
            if (trees == -1) {
                return 0;
            }
            return (trees > val) ? 1 : 0;
        }
        public Integer checkCars(Integer val) {
            if (cars == -1) {
                return 0;
            }
            return (cars == val) ? 1 : 0;
        }
        public Integer checkPerfumes(Integer val) {
            if (perfumes == -1) {
                return 0;
            }
            return (perfumes == val) ? 1 : 0;
        }

        public Integer calculateScore(Integer children, Integer cats, Integer samoyeds, Integer pomerians, Integer akitas,
                              Integer vizslas, Integer goldfish, Integer trees, Integer cars, Integer perfumes) {
            return List.of(checkChildren(children), checkCats(cats), checkSamoyeds(samoyeds), checkPomerians(pomerians),
                    checkAkitas(akitas), checkVizslas(vizslas), checkGoldfish(goldfish), checkTrees(trees), checkCars(cars),
                    checkPerfumes(perfumes)).stream().mapToInt(i -> i).sum();
        }

        public Integer calculateRetroScore(Integer children, Integer cats, Integer samoyeds, Integer pomerians, Integer akitas,
                                           Integer vizslas, Integer goldfish, Integer trees, Integer cars, Integer perfumes) {
            return List.of(checkChildren(children), checkRetroCats(cats), checkSamoyeds(samoyeds),
                    checkRetroPomerians(pomerians), checkAkitas(akitas), checkVizslas(vizslas), checkRetroGoldfish(goldfish),
                    checkRetroTrees(trees), checkCars(cars), checkPerfumes(perfumes)).stream().mapToInt(i -> i).sum();
        }
        @Override
        public String toString() {
            return String.format("%d: children: %d, cats: %d, samoyeds: %d, pomerians: %d, akitas: %d, vizslas: %d, goldfish: %d, trees: %d, cars: %d, perfumes: %d",
                    number, children, cats, samoyeds, pomerians, akitas, vizslas, goldfish, trees, cars, perfumes);
        }
    }
    List<Aunt> aunts;
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2015,16,false,0);
        createAunts(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = findGoodAunt(3,7,2,3,0,0,5,3,2,1);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = findRetroencabulatorAunt(3,7,2,3,0,0,5,3,2,1);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public void createAunts(List<String> lines) {
        aunts = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\d{1,}: (\\S{1,} \\d{1,}(, )?)");
        for (String line : lines) {
            Integer index1 = line.indexOf(':');
            String numberStr = line.substring(0,index1);
            String numberSplit[] = numberStr.split(" ");
            Integer number = Integer.parseInt(numberSplit[1]);
            String itemsStr = line.substring(index1+2);
            String items[] = itemsStr.split(", ");
            Map<String, Integer> values = new HashMap<>();
            for (String item : items) {
                String split2[] = item.split(": ");
                values.put(split2[0],Integer.parseInt(split2[1]));
            }
            aunts.add(new Aunt(number, values.getOrDefault("children",-1),
                    values.getOrDefault("cats",-1),
                    values.getOrDefault("samoyeds",-1),
                    values.getOrDefault("pomerians",-1),
                    values.getOrDefault("akitas",-1),
                    values.getOrDefault("vizslas",-1),
                    values.getOrDefault("goldfish",-1),
                    values.getOrDefault("trees",-1),
                    values.getOrDefault("cars",-1),
                    values.getOrDefault("perfumes", -1)));
        }
    }

    public Integer findGoodAunt(Integer children, Integer cats, Integer samoyeds, Integer pomerians, Integer akitas,
                                Integer vizslas, Integer goldfish, Integer trees, Integer cars, Integer perfumes) {
        Integer goodAuntScore = aunts.stream()
                .mapToInt(a -> a.calculateScore(children, cats, samoyeds,pomerians,akitas,vizslas,goldfish,trees,cars,perfumes))
                .max().getAsInt();
        return aunts.stream()
                .filter(a -> a.calculateScore(children, cats, samoyeds,pomerians,akitas,vizslas,goldfish,trees,cars,perfumes) == goodAuntScore)
                .collect(Collectors.toList()).get(0).number;
    }

    public Integer findRetroencabulatorAunt(Integer children, Integer cats, Integer samoyeds, Integer pomerians, Integer akitas,
                                Integer vizslas, Integer goldfish, Integer trees, Integer cars, Integer perfumes) {
        Integer goodAuntScore = aunts.stream()
                .mapToInt(a -> a.calculateRetroScore(children, cats, samoyeds,pomerians,akitas,vizslas,goldfish,trees,cars,perfumes))
                .max().getAsInt();
        return aunts.stream()
                .filter(a -> a.calculateRetroScore(children, cats, samoyeds,pomerians,akitas,vizslas,goldfish,trees,cars,perfumes) == goodAuntScore)
                .collect(Collectors.toList()).get(0).number;
    }

}
