#ifndef ADJACENCY_H
#define ADJACENCY_H

#include <QDebug>
#include <QString>
#include <QVector>
#include <iostream>

class Adjacency {
private:
    int numVertex, numEdge;
    int **matrix;
    bool *used;
    int *d;
    int *prev;
    QString *name;

public:
    Adjacency(int numVert)
    {
        int i;
        numVertex = numVert;
        numEdge = 0;
        matrix = (int**) new int* [numVertex];
        used = new bool[numVertex];
        d = new int[numVertex];
        prev = new int[numVertex];
        name = new QString[numVertex];
        for (i = 0; i < numVertex; ++i)
            matrix[i] = new int[numVertex];
        for (i = 0; i < numVertex; ++i)
            for (int j = 0; j < numVertex; ++j)
                matrix[i][j] = -1;
    }

    ~Adjacency() {
        delete[] used;
        delete[] d;
        delete[] prev;
        delete[] name;
        for (int i = 0; i < numVertex; ++i)
            delete[] matrix[i];
        delete[] matrix;
    }

    int getVertexSize() { return numVertex; }
    int getEdgeSize() { return numEdge; }

    int first(int vert) {
        for (int i = 0; i < numVertex; ++i)
            if (matrix[vert][i] != 0)
                return i;
        return numVertex;
    }

    int next(int v, int w) {
        for (int i = w+1; i < numVertex; ++i)
            if (matrix[v][i] != 0)
                return i;
        return numVertex;
    }

    void setEdge(int v1, int v2, int wt) {
        if (wt > 0) {
            if (matrix[v1][v2] == 0)
                ++numEdge;
            matrix[v1][v2] = wt;
        }
    }

    void delEdge(int v1, int v2) {
        if (matrix[v1][v2] != 0)
            --numEdge;
        matrix[v1][v2] = 0;
    }

    bool isEdge(int v1, int v2) { return matrix[v1][v2] != 0; }
    int weight(int v1, int v2) { return matrix[v1][v2]; }
    int getMark(int v) { return used[v]; }
    void setMark(int v, int val) { used[v] = val; }

    void setName(QString na, int pos) { name[pos] = na; }
    QString getName(int pos) { return name[pos]; }
    int getLength(int s) { return d[s]; }

    void dijkstra(int s) {
        std::fill(d, d+numVertex, -1);
        std::fill(used, used+numVertex, false);
        std::fill(prev, prev+numVertex, -1);
        d[s] = 0;
        while(true) {
            int v = -1;
            for (int u = 0; u < numVertex; ++u)
                if (!used[u] && (v == -1 || d[u] < d[v]))
                    v = u;
            if (v == -1) break;
            used[v] = true;
            for (int u = 0; u < numVertex; ++u) {
                if (d[u] > d[v] + matrix[v][u]) {
                    d[u] = d[v] + matrix[v][u];
                    qDebug() << d[u];
                    prev[u] = v;
                }
            }
        }
    }

    void floyd() {
        for (int k = 0; k < numVertex; ++k)
            for (int i = 0; i < numVertex; ++i)
                for (int j = 0; j < numVertex; ++j)
                    matrix[i][j] = std::min(matrix[i][j], matrix[j][k]+matrix[k][j]);
    }

    QVector<int> getPath(int t) {
        QVector<int> path;
        for (; t != -1; t = prev[t])
            path.push_back(t);
        std::reverse(path.begin(), path.end());
        return path;
    }
};
        
#endif
