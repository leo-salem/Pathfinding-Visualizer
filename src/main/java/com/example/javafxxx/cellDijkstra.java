package com.example.javafxxx;

import java.util.Objects;

public class cellDijkstra {
    int x, y, cost;

    public cellDijkstra(int x, int y, int cost) {
        this.x = x;
        this.y = y;
        this.cost = cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        cellDijkstra that = (cellDijkstra) o;
        return x == that.x && y == that.y && cost == that.cost;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, cost);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", cost: " + cost + ")";
    }
}