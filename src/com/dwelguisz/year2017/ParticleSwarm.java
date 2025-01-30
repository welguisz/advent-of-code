package com.dwelguisz.year2017;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

public class ParticleSwarm extends AoCDay {
    
    public static class Particle {
        Integer id;
        Long px;
        Long py; 
        Long pz;
        Long vx;
        Long vy;
        Long vz;
        Long ax;
        Long ay;
        Long az;
        
        public Particle(Integer id, Long px, Long py, Long pz, Long vx, Long vy, Long vz, Long ax, Long ay, Long az) {
            this.id = id;
            this.px = px;
            this.py = py;
            this.pz = pz;
            this.vx = vx;
            this.vy = vy;
            this.vz = vz;
            this.ax = ax;
            this.ay = ay;
            this.az = az;
        }

        public boolean collision(Particle that) {
            return (this.px == that.px) && (this.py == that.py) && (this.pz == that.pz);
        }

        public void oneMoreTick() {
            vx += ax;
            vy += ay;
            vz += az;
            px += vx;
            py += vy;
            pz += vz;
        }
        
        public Long getManhattanDistance(Long x, Long y, Long z) {
            return Math.abs(px-x) + Math.abs(py-y) + Math.abs(pz - z);
        }

        public Long absoluateAcceleartion() {
            return Math.abs(ax) + Math.abs(ay) + Math.abs(az);
        }
            
    }
    
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2017,20,false,0);
        List<Particle> particles = createInitialParticles(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(particles);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(particles);
        timeMarkers[3] = Instant.now().toEpochMilli();
        Integer part1 = solutionPart1(particles);
    }

    public Integer solutionPart1(List<Particle> initialParticles) {
        Long smallestAccel = initialParticles.stream().map(Particle::absoluateAcceleartion).min(Long::compareTo).get();
        Particle smP = initialParticles.stream()
                .filter(p -> p.absoluateAcceleartion() == smallestAccel)
                .collect(Collectors.toList()).get(0);
        return smP.id;

    }

    public Integer solutionPart2(List<Particle> initialParticles) {
        List<Particle> particles = new ArrayList<>(initialParticles);
        for(int i = 0; i < 1000;i++) {
            Set<Particle> removeIndex = new HashSet<>();
            for (int x = 0; x < particles.size(); x++) {
                for (int y = x+1; y < particles.size(); y++) {
                    if (particles.get(x).collision(particles.get(y))) {
                        removeIndex.add(particles.get(x));
                        removeIndex.add(particles.get(y));
                    }
                }
            }
            for (Particle p : removeIndex) {
                particles.remove(p);
            }
            particles.stream().forEach(p -> p.oneMoreTick());
        }
        return particles.size();
    }

    public List<Particle> createInitialParticles(List<String> lines) {
        List<Particle> particles = new ArrayList<>();
        int id = 0;
        for (String line : lines) {
            String infoBlobs[] = line.split(", ");
            infoBlobs[0] = infoBlobs[0].substring(3, infoBlobs[0].length()-1);
            infoBlobs[1] = infoBlobs[1].substring(3, infoBlobs[1].length()-1);
            infoBlobs[2] = infoBlobs[2].substring(3, infoBlobs[2].length()-1);
            String positions[] = infoBlobs[0].split(",");
            String velocities[] = infoBlobs[1].split(",");
            String acclerations[] = infoBlobs[2].split(",");
            particles.add( new Particle(id,
                    Long.parseLong(positions[0].replace(" ", "")),
                    Long.parseLong(positions[1].replace(" ", "")),
                    Long.parseLong(positions[2].replace(" ", "")),
                    Long.parseLong(velocities[0].replace(" ", "")),
                    Long.parseLong(velocities[1].replace(" ", "")),
                    Long.parseLong(velocities[2].replace(" ", "")),
                    Long.parseLong(acclerations[0].replace(" ", "")),
                    Long.parseLong(acclerations[1].replace(" ", "")),
                    Long.parseLong(acclerations[2].replace(" ", ""))));
            id++;
        }
        return particles;
    }
}
