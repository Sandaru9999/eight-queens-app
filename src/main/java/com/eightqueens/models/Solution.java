package com.eightqueens.models;
import java.util.Arrays;
public class Solution {
 private int[] positions; // row -> column
 public Solution(int[] positions) {
 this.positions = positions;
 }
 public int[] getPositions() {
 return positions;
 }
 @Override
 public String toString() {
 return Arrays.toString(positions);
 }
 @Override
 public boolean equals(Object o) {
 if (this == o) return true;
 if (!(o instanceof Solution)) return false;
 Solution other = (Solution) o;
 return Arrays.equals(positions, other.positions);
 }
 @Override
 public int hashCode() {
 return Arrays.hashCode(positions);
 }
}
