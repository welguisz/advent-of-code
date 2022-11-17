package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;

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

        public boolean checkChildren(Integer val) {
            if (children < 0) {
                return true;
            }
            return children == val;
        }
        public boolean checkCats(Integer val) {
            if (cats < 0) {
                return true;
            }
            return cats == val;
        }
        public boolean checkRetroCats(Integer val) {
            if (cats < 0) {
                return true;
            }
            return cats > val;
        }
        public boolean checkSamoyeds(Integer val) {
            if (samoyeds < 0) {
                return true;
            }
            return samoyeds == val;
        }
        public boolean checkPomerians(Integer val) {
            if (pomerians < 0) {
                return true;
            }
            return pomerians == val;
        }
        public boolean checkRetroPomerians(Integer val) {
            if (pomerians < 0) {
                return true;
            }
            return pomerians < val;
        }
        public boolean checkAkitas(Integer val) {
            if (akitas < 0) {
                return true;
            }
            return akitas == val;
        }
        public boolean checkVizslas(Integer val) {
            if (vizslas < 0) {
                return true;
            }
            return vizslas == val;
        }
        public boolean checkGoldfish(Integer val) {
            if (goldfish < 0) {
                return true;
            }
            return goldfish == val;
        }
        public boolean checkRetroGoldfish(Integer val) {
            if (goldfish < 0) {
                return true;
            }
            return goldfish < val;
        }
        public boolean checkTrees(Integer val) {
            if (trees < 0) {
                return true;
            }
            return trees == val;
        }
        public boolean checkRetroTrees(Integer val) {
            if (trees < 0) {
                return true;
            }
            return trees > val;
        }
        public boolean checkCars(Integer val) {
            if (cars < 0) {
                return true;
            }
            return cars == val;
        }
        public boolean checkPerfumes(Integer val) {
            if (perfumes < 0) {
                return true;
            }
            return perfumes == val;
        }
    }
    List<Aunt> aunts;
    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2015/day16/input.txt");
        createAunts(lines);
        Integer part1 = findGoodAunt(3,7,2,3,0,0,5,3,2,1);
        System.out.println(String.format("Part 1 Answer: %d", part1));
        Integer part2 = findRetroencabulatorAunt(3,7,2,3,0,0,5,3,2,1);
        System.out.println(String.format("Part 2 Answer: %d", part2));
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
        List<Aunt> goodAunt = aunts.stream()
                .filter(a -> a.checkChildren(children))
                .filter(a -> a.checkCats(cats))
                .filter(a -> a.checkSamoyeds(samoyeds))
                .filter(a -> a.checkPomerians(pomerians))
                .filter(a -> a.checkAkitas(akitas))
                .filter(a -> a.checkVizslas(vizslas))
                .filter(a -> a.checkGoldfish(goldfish))
                .filter(a -> a.checkTrees(trees))
                .filter(a -> a.checkCars(cars))
                .filter(a -> a.checkPerfumes(perfumes))
                .collect(Collectors.toList());
        return goodAunt.get(0).number;
    }

    public Integer findRetroencabulatorAunt(Integer children, Integer cats, Integer samoyeds, Integer pomerians, Integer akitas,
                                Integer vizslas, Integer goldfish, Integer trees, Integer cars, Integer perfumes) {
        List<Aunt> goodAunt = aunts.stream()
                .filter(a -> a.checkChildren(children))
                .filter(a -> a.checkRetroCats(cats))
                .filter(a -> a.checkSamoyeds(samoyeds))
                .filter(a -> a.checkRetroPomerians(pomerians))
                .filter(a -> a.checkAkitas(akitas))
                .filter(a -> a.checkVizslas(vizslas))
                .filter(a -> a.checkRetroGoldfish(goldfish))
                .filter(a -> a.checkRetroTrees(trees))
                .filter(a -> a.checkCars(cars))
                .filter(a -> a.checkPerfumes(perfumes))
                .collect(Collectors.toList());
        StringBuffer sb = new StringBuffer();
        for (Aunt aunt : goodAunt) {
            sb.append(aunt.number);
            sb.append(", ");
        }
        System.out.println(sb.toString());
        return goodAunt.get(0).number;
    }

}
