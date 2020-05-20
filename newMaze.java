
import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;
import java.util.*;

//represents a list of objects
interface IList<T> extends Iterable<T> {
  // determines if list is empty
  boolean isEmpty();

  // returns the length of our list
  int len();

  // for iterating over the list
  Iterator<T> iterator();

  // adds an element to the list
  IList<T> add(T t);

  // gets data at index i
  T get(int i);

  // appends a list to the list
  IList<T> append(IList<T> l);

  // checks if list has another value left
  boolean hasNext();

  // gets data value at this point
  T getData();

  // gets the rest of the list
  IList<T> getNext();
}

//represents an empty list
class Empty<T> implements IList<T>, Iterable<T> {
  // finds the length of our list
  public int len() {
    return 0;
  }

  // determines if list is empty
  public boolean isEmpty() {
    return true;
  }

  // for iterating over the list
  public Iterator<T> iterator() {
    return new ListIterator<T>(this);
  }

  // adds an element to the list
  public IList<T> add(T t) {
    return new Cons<T>(t, new Empty<T>());
  }

  // gets data at index i
  public T get(int i) {
    return null;
  }

  // appends an element to the list
  public IList<T> append(IList<T> l) {
    return l;
  }

  // checks if list has another value left
  public boolean hasNext() {
    return false;
  }

  // gets data value at this point
  public T getData() {
    return null;
  }

  // gets the rest of the list
  public IList<T> getNext() {
    throw new UnsupportedOperationException();
  }
}

//represents a list with an element of a list
class Cons<T> implements IList<T>, Iterable<T> {
  // The data of this node
  T first;
  // the rest of the list
  IList<T> rest;

  Cons(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }

  // finds the length of our list
  public int len() {
    return 1 + this.rest.len();
  }

  // determines if list is empty
  public boolean isEmpty() {
    return false;
  }

  // for iterating over the list
  public Iterator<T> iterator() {
    return new ListIterator<T>(this);
  }

  // adds an element to the list
  public IList<T> add(T t) {
    if (rest.isEmpty()) {
      rest = new Cons<T>(t, new Empty<T>());
    }
    else {
      rest = rest.add(t);
    }
    return this;
  }

  // gets data at index i
  public T get(int i) {
    if (i == 0) {
      return this.first;
    }
    else {
      return this.rest.get(i--);
    }
  }

  // appends a list to the list
  public IList<T> append(IList<T> l) {
    if (rest.isEmpty()) {
      rest = l;
    }
    else {
      rest.append(l);
    }
    return this;
  }

  // checks if list has another value left
  public boolean hasNext() {
    return true;
  }

  // gets data value at this point
  public T getData() {
    return this.first;
  }

  // gets the rest of the list
  public IList<T> getNext() {
    return this.rest;
  }
}

//for iterating over our list
class ListIterator<T> implements Iterator<T> {
  IList<T> curr;

  ListIterator(IList<T> curr) {
    this.curr = curr;
  }

  // returns true if there's at least one value left in this iterator
  public boolean hasNext() {
    return curr.hasNext();
  }

  // returns the next value and advances the iterator
  public T next() {
    T temp = this.curr.getData();
    this.curr = this.curr.getNext();
    return temp;
  }

  // no need to implement this since it is never used
  public void remove() {
    throw new UnsupportedOperationException();
  }
}

//a queue data structure
class Queue<T> {
  // the contents of our queue
  Deque<T> contents;

  Queue() {
    this.contents = new Deque<T>();
  }

  Queue(IList<T> ts) {
    contents = new Deque<T>();
    for (T t : ts) {
      contents.addAtTail(t);
    }
  }

  // adds an item to the tail of the list
  void enqueue(T item) {
    contents.addAtTail(item);
  }

  // determines if our contents are empty
  boolean isEmpty() {
    return contents.size() == 0;
  }

  // removes and returns the tail of the list
  T dequeue() {
    return contents.removeFromTail();
  }
}

//To represent a Deque list
class Deque<T> implements Iterable<T> {
  Sentinel<T> header;

  public Iterator<T> iterator() {
    return new DequeIterator<T>(this.header);
  }

  Deque() {
    this.header = new Sentinel<T>();
  }

  Deque(Sentinel<T> header) {
    this.header = header;
  }

  // returns the total number of nodes in this Deque
  int size() {
    return this.header.size();
  }

  // Adds a new node with data t at the head of the Deque
  T addAtHead(T t) {
    return header.addAtHead(t);
  }

  // Adds a new node with data t at the tail of the Deque
  T addAtTail(T t) {
    return header.addAtTail(t);
  }

  // removes a the node at the head of the Deque
  T removeFromHead() {
    if (this.size() == 0) {
      throw new RuntimeException("Cannot remove from an empty list");
    }
    return header.removeFromHead();
  }

  // removes a the node at the tail of the Deque
  T removeFromTail() {
    if (this.size() == 0) {
      throw new RuntimeException("Cannot remove from an empty list");
    }
    return header.removeFromTail();
  }



  // finds a node that satisfies the predicate, otherwise returns the header
  ANode<T> find(IPred<T> p) {
    return header.find(p, true);
  }

  // removes a node from the deque
  void removeNode(ANode<T> a) {
    if (!a.equals(header)) {
      header.removeNodeFirst(a);
    }
  }
}

class DequeIterator<T> implements Iterator<T> {
  ANode<T> curr;

  DequeIterator(ANode<T> curr) {
    this.curr = curr;
  }

  // returns true if there's at least one value left in this iterator
  public boolean hasNext() {
    return curr.hasNext();
  }

  // returns the next value and advances the iterator
  public T next() {
    T temp = this.curr.next.getData();
    this.curr = this.curr.next;
    return temp;
  }

  // not needed for our purposes but required by Iterator
  public void remove() {
    throw new UnsupportedOperationException();
  }
}

//An abstract class to represent a node, either a sentinel or a regular node
abstract class ANode<T> {
  ANode<T> next;
  ANode<T> prev;

  // returns the total number of nodes in this deque
  int size(ANode<T> s) {
    if (this.next.equals(s)) {
      return 1;
    }
    else {
      return 1 + this.next.size(s);
    }
  }

  // removes this node from the deque
  T remove() {
    this.prev.next = this.next;
    this.next.prev = this.prev;
    return this.getData();
  }

  // assists in finding a node that meets the comparator c
  ANode<T> find(IPred<T> p) {
    return this;
  }

  // assists in removing the node a from the deque
  void removeNode(ANode<T> a) {
    if (this.equals(a)) {
      this.remove();
    }
    else {
      this.next.removeNode(a);
    }
  }

  // returns the data for this node
  T getData() {
    return null;
  }

  // determines whether this node has a next value
  boolean hasNext() {
    return !next.isSentinel();
  }

  // determines if this node is a sentinel
  boolean isSentinel() {
    return false;
  }
}

//a class to represent an ordinary node
class Node<T> extends ANode<T> {
  T data;

  Node(T data) {
    this.data = data;
    this.next = null;
    this.prev = null;
  }

  // initializes the node and places it in the deque
  Node(T data, ANode<T> next, ANode<T> prev) {
    if ((next == null) || (prev == null)) {
      throw new IllegalArgumentException("Cannot accept null node");
    }
    this.data = data;
    this.next = next;
    this.prev = prev;
    prev.next = this;
    next.prev = this;
  }

  // assists in finding a node in the deque that meets predicate p
  ANode<T> find(IPred<T> p) {
    if (p.apply(this.data)) {
      return this;
    }
    else {
      return this.next.find(p);
    }
  }

  // returns the data for this node
  T getData() {
    return this.data;
  }
}


//a class to represent a sentinel, a marker for the deque
class Sentinel<T> extends ANode<T> {

  Sentinel() {
    this.next = this;
    this.prev = this;
  }

  // assists in returning the total number of nodes in this deque
  int size() {
    if (this.next.equals(this)) {
      return 0;
    }
    return this.next.size(this);
  }

  // adds a node with data t to the head
  T addAtHead(T t) {
    new Node<T>(t, this.next, this);
    return this.next.getData();
  }

  // adds a node with data t to the tail
  T addAtTail(T t) {
    new Node<T>(t, this, this.prev);
    return this.next.getData();
  }

  // removes a node from the head of the deque
  T removeFromHead() {
    return this.next.remove();
  }

  // removes a node from the tail of the deque
  T removeFromTail() {
    return this.prev.remove();
  }

  // assists in finding a node that meets predicate p
  // boolean b is true if this is the first time going
  // through this sentinel searching for the node
  ANode<T> find(IPred<T> p, boolean b) {
    return this.next.find(p);
  }

  // assists in removing a node a from the deque
  // this function indicates that this is the first time going
  // through this sentinel searching for the node
  void removeNodeFirst(ANode<T> a) {
    this.next.removeNode(a);
  }

  // assists in removing a node a from the deque
  // this function indicates that this is the second time going
  // through this sentinel searching for the node, thus it nullifies
  // the ANode function since the node cannot be found.
  void removeNode(ANode<T> a) {
    return;
  }

  // determines if this node is a sentinel
  boolean isSentinel() {
    return true;
  }
}



//for comparing values
interface IComparator<T> {
  boolean apply(T t1, T t2);
}

//Represents a boolean-valued question over values of type T
interface IPred<T> {
  public boolean apply(T t);
}


//represents a stack of type T
class Stack<T> {
  // the contents of the stack
  Deque<T> contents;

  Stack() {
    this.contents = new Deque<T>();
  }

  Stack(IList<T> ts) {
    contents = new Deque<T>();
    for (T t : ts) {
      contents.addAtTail(t);
    }
  }

  // adds an item to the head of the list
  void push(T item) {
    contents.addAtHead(item);
  }

  // determines if our list is empty
  boolean isEmpty() {
    return contents.size() == 0;
  }

  // removes and returns the head of the list
  T pop() {
    return contents.removeFromHead();
  }
}

//compares edges by weight
class CompareEdges implements IComparator<Edge> {
  public boolean apply(Edge e1, Edge e2) {
    return e1.weight < e2.weight;
  }
}



// a vertex in a graph
class Vertex {
  int x;
  int y;
  // have we visited this vertex?
  boolean visited;
  // whether this vertex is part of the solution path
  boolean path;
  // all Edges that stem from this vertex
  ArrayList<Edge> outEdges;

  Vertex(int x, int y) {
    this.x = x;
    this.y = y;
    this.outEdges = new ArrayList<Edge>();
    this.visited = false;
    this.path = false;
  }

  // used for identifying each vertex
  int toIdentifier() {
    return 1000 * y + x;
  }
}

// an edge in a graph
class Edge {
  Vertex from;
  Vertex to;
  int weight;

  Edge(Vertex from, Vertex to, int weight) {
    this.from = from;
    this.to = to;
    this.weight = weight;
  }
}

//an iterator for our breadth first search
class BreadthFirst extends Search {
  // this is our current List of Vertices
  Queue<Vertex> lov;

  BreadthFirst(IList<Vertex> list) {
    this.lov = new Queue<Vertex>();
    lov.enqueue(list.getData());
    list.getData().visited = true;
    cameFromEdge = new HashMap<Integer, Vertex>();
  }

  // determines if we have another task in our List of Vertices
  public boolean hasNext() {
    return !lov.isEmpty();
  }

  // iterates once through our bfs procedure
  public Queue<Vertex> next() {

    Vertex u = lov.dequeue();
    for (Edge e : u.outEdges) {
      if (!e.to.visited) {
        cameFromEdge.put(e.to.toIdentifier(), e.from);
        if (e.to.x == MazeWorld.width - 1 && e.to.y == MazeWorld.height - 1) {
          reconstruct(cameFromEdge, e.to);
          lov = new Queue<Vertex>();
        }
        else {
          e.to.visited = true;
          lov.enqueue(e.to);
        }
      }
    }
    return lov;
  }
}

//an iterator for our depth first search
class DepthFirst extends Search {
  //this is our current list of vertices 
  Stack<Vertex> lov;

  DepthFirst(IList<Vertex> list) {
    this.lov = new Stack<Vertex>();
    lov.push(list.getData());
    list.getData().visited = true;
    cameFromEdge = new HashMap<Integer, Vertex>();
  }

  // determines if we have another task in our lov
  public boolean hasNext() {
    return !lov.isEmpty();
  }

  // iterates once through our dfs procedure
  public Stack<Vertex> next() {
    Vertex u = lov.pop();
    for (Edge e : u.outEdges) {
      if (!e.to.visited) {
        cameFromEdge.put(e.to.toIdentifier(), e.from);
        if (e.to.x == MazeWorld.width - 1 && e.to.y == MazeWorld.height - 1) {
          reconstruct(cameFromEdge, e.to);
          lov = new Stack<Vertex>();
        }
        else {
          lov.push(u);
          e.to.visited = true;
          lov.push(e.to);
          break;
        }
      }
    }
    return lov;
  }
}

//an iterator for a human player
class Player extends Search {
  // our current vertex
  Vertex current;
  // whether we have finished the maze
  boolean finished;

  Player(IList<Vertex> list) {
    current = list.getData();
    cameFromEdge = new HashMap<Integer, Vertex>();
    finished = false;
  }

  // determines if we have finished
  public boolean hasNext() {
    return !finished;
  }


  public Vertex move(boolean b, Edge e) {
    if (b) {
      current.visited = true;
      current.path = false;
      if (!e.to.visited) {
        cameFromEdge.put(e.to.toIdentifier(), e.from);
      }
      if (e.to.x == MazeWorld.width - 1 && e.to.y == MazeWorld.height - 1) {
        reconstruct(cameFromEdge, e.to);
      }
      else {
        current = e.to;
        current.path = true;
      }
    }
    return current;
  }

  // moves our current position to the left
  // and adds new paths to our hashtable
  public Vertex moveLeft() {
    for (Edge e : current.outEdges) {
      move(e.to.x == current.x - 1, e);
    }
    return current;
  }

  // moves our current position to the right
  // and adds new paths to our hashtable
  public Vertex moveRight() {
    for (Edge e : current.outEdges) {
      move(e.to.x == current.x + 1, e);
    }
    return current;
  }

  // moves our current position to the down
  // and adds new paths to our hashtable
  public Vertex moveDown() {
    for (Edge e : current.outEdges) {
      move(e.to.y == current.y + 1, e);
    }
    return current;
  }

  // moves our current position to the up
  // and adds new paths to our hashtable
  public Vertex moveUp() {
    for (Edge e : current.outEdges) {
      move(e.to.y == current.y - 1, e);
    }
    return current;
  }

  // constructs our path back to the start from the end
  void reconstruct(HashMap<Integer, Vertex> h, Vertex next) {
    while (h.containsKey(next.toIdentifier())) {
      next.path = true;
      next = h.get(next.toIdentifier());
    }
  }
}

//our super class for our various searches
abstract class Search {
  // for keeping track of our path
  HashMap<Integer, Vertex> cameFromEdge;

  // constructs our path to the start from the end
  void reconstruct(HashMap<Integer, Vertex> h, Vertex next) {
    while (h.containsKey(next.toIdentifier())) {
      next.path = true;
      next = h.get(next.toIdentifier());
    }
  }
}

// our game world
class MazeWorld extends World {
  public static Random randomNum = new Random();
  // width of the world in cells
  public static int width = randomNum.nextInt(100);
  // height of the world in cells
  public static int height = randomNum.nextInt(60);
  // scale of the world
  static int scale = 10;

  // all of our vertices
  IList<Vertex> vertices;
  // all of the walls
  IList<Edge> walls;

  // constructs a maze

  //via BFS, DFS or manually
  boolean bfs;
  boolean dfs;
  boolean manual;
  BreadthFirst b;
  DepthFirst d;
  Player p;


  MazeWorld() {
    boardSetup();
  }



  // initializes up our maze by constructing the vertices and walls. 
  void boardSetup() {
    ArrayList<ArrayList<Vertex>> vInt = generateInitVertices();
    ArrayList<Edge> allEdges = getEdges(vInt);
    //getting naming error in java style checker, not sure why
    vInt = kruskalVertice(vInt);
    walls = getWalls(vInt, allEdges);
    vertices = new Empty<Vertex>();
    for (ArrayList<Vertex> vList : vInt) {
      for (Vertex vt : vList) {
        vertices = vertices.add(vt);
      }
    }
    bfs = false;
    dfs = false;
    manual = false;
    b = new BreadthFirst(vertices);
    d = new DepthFirst(vertices);
    p = new Player(vertices);

  }

  // gets a list of all the valid walls that fit in our maze
  IList<Edge> getWalls(ArrayList<ArrayList<Vertex>> v, ArrayList<Edge> all) {
    IList<Edge> finalEdges = new Empty<Edge>();
    for (Edge e : all) {
      boolean valid = true;
      for (ArrayList<Vertex> l : v) {
        for (Vertex vt : l) {
          for (Edge e2 : vt.outEdges) {
            if (e.equals(e2) || (e.to == e2.from && e.from == e2.to)) {
              valid = false;
            }
          }
        }
      }
      if (valid) {
        finalEdges = finalEdges.add(e);
      }
    }
    return finalEdges;
  }

  // returns an arraylist of every edge in a list of vertices
  ArrayList<Edge> getEdges(ArrayList<ArrayList<Vertex>> v) {
    ArrayList<Edge> all = new ArrayList<Edge>();
    for (ArrayList<Vertex> verts : v) {
      for (Vertex vt : verts) {
        for (Edge ed : vt.outEdges) {
          all.add(ed);
        }
      }
    }
    return all;
  }

  // generates our ArrayList of arraylist of vertices
  ArrayList<ArrayList<Vertex>> generateInitVertices() {
    ArrayList<ArrayList<Vertex>> vertices = new ArrayList<ArrayList<Vertex>>();
    for (int x = 0; x < width; x++) {
      ArrayList<Vertex> temp = new ArrayList<Vertex>();
      for (int y = 0; y < height; y++) {
        temp.add(new Vertex(x, y));
      }
      vertices.add(temp);
    }
    Random r = new Random();
    for (ArrayList<Vertex> vList : vertices) {
      for (Vertex v : vList) {
        if (v.x != 0) {
          v.outEdges.add(new Edge(v, vertices.get(v.x - 1).get(v.y), r.nextInt(1000)));
        }
        if (v.x != width - 1) {
          v.outEdges.add(new Edge(v, vertices.get(v.x + 1).get(v.y), r.nextInt(1000)));
        }
        if (v.y != 0) {
          v.outEdges.add(new Edge(v, vertices.get(v.x).get(v.y - 1), r.nextInt(1000)));
        }
        if (v.y != height - 1) {
          v.outEdges.add(new Edge(v, vertices.get(v.x).get(v.y + 1), r.nextInt(1000)));
        }
      }
    }
    return vertices;
  }

  // finds our actual edges for our maze based on weights
  // and assigns them to our vertices
  ArrayList<ArrayList<Vertex>> kruskalVertice(ArrayList<ArrayList<Vertex>> v) {
    ArrayList<Edge> allEdges = getEdges(v);
    for (ArrayList<Vertex> i : v) {
      for (Vertex j : i) {
        j.outEdges = new ArrayList<Edge>();
      }
    }
    int totalCells = height * width;
    IList<Edge> loe = new Empty<Edge>();
    //getting naming error, not sure why
    ArrayList<Edge> allEdgesSorted = mergeHelp(allEdges);
    HashMap<Integer, Integer> hash = new HashMap<Integer, Integer>();
    for (int i = 0; i <= (1000 * height) + width; i++) {
      hash.put(i, i);
    }
    ArrayList<Edge> l = allEdgesSorted;
    while (loe.len() < totalCells - 1) {
      Edge e = l.get(0);
      if (this.find(hash, e.to.toIdentifier()) != this.find(hash, e.from.toIdentifier())) {
        loe = loe.add(e);
        e.from.outEdges.add(e);
        e.to.outEdges.add(new Edge(e.to, e.from, e.weight));
        int temp = (find(hash, e.to.toIdentifier()));
        hash.remove(find(hash, e.to.toIdentifier()));
        hash.put(temp, find(hash, e.from.toIdentifier()));
      }
      l.remove(0);
    }
    return v;
  }

  // used for finding values in our hashmap
  int find(HashMap<Integer, Integer> hashmap, int x) {
    if (hashmap.get(x) == x) {
      return x;
    }
    else {
      return find(hashmap, hashmap.get(x));
    }
  }

  // sorts our list of edges by merge sorting
  ArrayList<Edge> mergeHelp(ArrayList<Edge> l) {
    if (l.size() <= 1) {
      return l;
    }
    ArrayList<Edge> l1 = new ArrayList<Edge>();
    ArrayList<Edge> l2 = new ArrayList<Edge>();
    for (int i = 0; i < l.size() / 2; i++) {
      l1.add(l.get(i));
    }
    for (int i = l.size() / 2; i < l.size(); i++) {
      l2.add(l.get(i));
    }
    l1 = mergeHelp(l1);
    l2 = mergeHelp(l2);
    return merge(l1, l2);
  }

  // the merging step of our merge sort
  ArrayList<Edge> merge(ArrayList<Edge> l1, ArrayList<Edge> l2) {
    ArrayList<Edge> l3 = new ArrayList<Edge>();
    IComparator<Edge> c = new CompareEdges();
    while (l1.size() > 0 && l2.size() > 0) {
      if (c.apply(l1.get(0), l2.get(0))) {
        l3.add(l1.get(0));
        l1.remove(0);
      }
      else {
        l3.add(l2.get(0));
        l2.remove(0);
      }
    }
    while (l1.size() > 0) {
      l3.add(l1.get(0));
      l1.remove(0);
    }
    while (l2.size() > 0) {
      l3.add(l2.get(0));
      l2.remove(0);
    }
    return l3;
  }

  // generates an appropriate color given a vertex
  Color generateColor(Vertex v) {
    if (v.x == width - 1 && v.y == height - 1) {
      return Color.magenta;
    }
    else if (v.path) {
      return Color.blue;
    }
    else if (v.x == 0 && v.y == 0) {
      return Color.green;
    }
    else if (v.visited) {
      return Color.cyan;
    }
    else {
      return Color.gray;
    }
  }


  // draws the world scene
  public WorldScene makeScene() {
    WorldScene w = new WorldScene(width * scale, height * scale);
    for (Vertex v : vertices) {
      Color color = generateColor(v);
      w.placeImageXY(new RectangleImage(scale, scale, OutlineMode.SOLID, color),
          (v.x * scale) + (scale * 1 / 2), (v.y * scale) + (scale * 1 / 2));
    }
    for (Edge e : walls) {
      if (e.to.x == e.from.x) {
        w.placeImageXY(
            new RectangleImage(scale, scale / 10, OutlineMode.SOLID, Color.black),
            (e.to.x * scale) + (scale * 1 / 2),
            ((e.to.y + e.from.y) * scale / 2) + (scale * 1 / 2));
      }
      else {
        w.placeImageXY(
            new RectangleImage(scale / 10, scale, OutlineMode.SOLID, Color.black),
            ((e.to.x + e.from.x) * scale / 2) + (scale * 1 / 2),
            (e.to.y * scale) + (scale * 1 / 2));
      }
    }
    return w;
  }

  // does tasks on every tick
  public void onTick() {
    if (bfs) {
      if (b.hasNext()) {
        b.next();
      }
    }
    if (dfs) {
      if (d.hasNext()) {
        d.next();
      }
    }
  }

  // does tasks given a key event
  public void onKeyEvent(String ke) {
    if (ke.equals("b")) {
      bfs = true;
      dfs = false;
      manual = false;
      reset();
    }
    else if (ke.equals("d")) {
      bfs = false;
      dfs = true;
      manual = false;
      reset();
    }
    else if (ke.equals("p")) {
      bfs = false;
      dfs = false;
      manual = true;
      reset();
    }
    else if (ke.equals("r")) {
      boardSetup();
    }
    else if (manual) {
      if (p.hasNext()) {
        if (ke.equals("left")) {
          p.moveLeft();
        }
        else if (ke.equals("up")) {
          p.moveUp();
        }
        else if (ke.equals("right")) {
          p.moveRight();
        }
        else if (ke.equals("down")) {
          p.moveDown();
        }
      }
    }
  }

  // resets our game, but not our maze
  public void reset() {
    for (Vertex v : vertices) {
      v.path = false;
      v.visited = false;
    }
    b = new BreadthFirst(vertices);
    d = new DepthFirst(vertices);
    p = new Player(vertices);
  }
}

// for testing purposes
class ExamplesMazeGame1 {

  // test Vertex methods
  void testVertexMethods(Tester t) {
    Vertex v1 = new Vertex(2, 9);
    Vertex v2 = new Vertex(0, 0);

    t.checkExpect(v1.toIdentifier(), 9002);
    t.checkExpect(v2.toIdentifier(), 0);

  }

  void testGenerateColor(Tester t) {
    Vertex v3 = new Vertex(10, 10);
    Vertex v4 = new Vertex(0, 0);

    t.checkExpect(new MazeWorld().generateColor(v3), Color.gray);
    t.checkExpect(new MazeWorld().generateColor(v4), Color.green);
  }

  // test sorting algorithm
  void testMerges(Tester t) {
    MazeWorld m = new MazeWorld();

    Edge e1 = new Edge(null, null, 10);
    Edge e2 = new Edge(null, null, 15);
    Edge e3 = new Edge(null, null, 5);
    Edge e4 = new Edge(null, null, 30);
    Edge e5 = new Edge(null, null, 25);

    Edge ne1 = new Edge(null, null, 50);
    Edge ne2 = new Edge(null, null, 3);
    Edge ne3 = new Edge(null, null, 5);
    Edge ne4 = new Edge(null, null, 30);
    Edge ne5 = new Edge(null, null, 15);

    ArrayList<Edge> unsorted = new ArrayList<Edge>();
    ArrayList<Edge> sorted = new ArrayList<Edge>();
    ArrayList<Edge> sorted2 = new ArrayList<Edge>();
    ArrayList<Edge> merged = new ArrayList<Edge>();



    unsorted.add(e1);
    unsorted.add(e2);
    unsorted.add(e3);
    unsorted.add(e4);
    unsorted.add(e5);

    sorted.add(e3);
    sorted.add(e1);
    sorted.add(e2);
    sorted.add(e5);
    sorted.add(e4);

    sorted2.add(ne2);
    sorted2.add(ne3);
    sorted2.add(ne5);
    sorted2.add(ne4);
    sorted2.add(ne1);

    merged.add(ne2);
    merged.add(e3);
    merged.add(ne3);
    merged.add(e1);
    merged.add(e2);
    merged.add(ne5);
    merged.add(e5);
    merged.add(ne4);
    merged.add(e4);
    merged.add(ne1);


    t.checkExpect(m.mergeHelp(unsorted), sorted);
    t.checkExpect(m.merge(sorted, sorted2), merged);
  }

  //to test search methods
  void testSearches(Tester t) {
    Vertex v1 = new Vertex(10, 10);
    Vertex v2 = new Vertex(10, 11);
    Vertex v3 = new Vertex(10, 12);

    Edge e1 = new Edge(v1, v2, 1);
    Edge e2 = new Edge(v2, v3, 1);

    IList<Vertex> map = new Cons<Vertex>(v1, new Cons<Vertex>(v2, new Cons<Vertex>(
        v3, new Empty<Vertex>())));

    Player player = new Player(map);
    DepthFirst dp = new DepthFirst(map);
    BreadthFirst bp = new BreadthFirst(map);

    t.checkExpect(player.current, v1);
    t.checkExpect(player.finished, false);

    t.checkExpect(bp.hasNext(), true);
    t.checkExpect(bp.next(), new Queue<Vertex>());

    t.checkExpect(dp.hasNext(), true);
    t.checkExpect(dp.next(), new Stack<Vertex>());

    t.checkExpect(player.move(true, e1), v2);

    t.checkExpect(player.current, v2);
    t.checkExpect(player.finished, false);

    t.checkExpect(bp.hasNext(), false);
    t.checkExpect(bp.next(), v3);

    t.checkExpect(dp.hasNext(), false);
    t.checkExpect(dp.next(), v3);

    t.checkExpect(player.move(true, e2), v3);

    player.finished = true;

    t.checkExpect(player.current, v3);
    t.checkExpect(player.finished, true);

    Queue<Vertex> q = new Queue<Vertex>(map);

    q.enqueue(new Vertex(5, 5));

    Deque<Vertex> dq = new Deque<Vertex>();
    dq.addAtHead(v1);
    dq.addAtTail(v2);
    dq.addAtTail(new Vertex(5, 5));


    t.checkExpect(q.isEmpty(), false);
    t.checkExpect(q.contents, dq);

    Stack<Vertex> stack = new Stack<Vertex>(map);

    t.checkExpect(stack.isEmpty(), false);
    dq.removeFromHead();
    t.checkExpect(stack.pop(), dq);

    t.checkExpect(dp.next(), v2);
    t.checkExpect(bp.hasNext(), true);

  }

  // test other list methods
  void testListMethods(Tester t) {
    IList<Integer> mt = new Empty<Integer>();
    IList<Integer> l1 = new Cons<Integer>(1,
        new Cons<Integer>(2, new Cons<Integer>(3, 
            new Cons<Integer>(4, mt))));

    t.checkExpect(mt.len(), 0);
    t.checkExpect(l1.len(), 4);

    t.checkExpect(mt.append(l1), l1);
    t.checkExpect(l1.append(mt), l1);

    t.checkExpect(mt.getData(), null);
    t.checkExpect(l1.getData(), 1);

    t.checkExpect(l1.getNext(), new Cons<Integer>(2 , new Cons<Integer>(3 , 
        new Cons<Integer>(4 , mt))));
  }

  //Node and
  //Sentinel class methods
  void testNAN (Tester t){

    ANode<Integer> s1 = new Sentinel<Integer>();
    ANode<Integer> s2 = new Sentinel<Integer>();

    ANode<Integer> n1 = new Node<Integer>(1); 
    ANode<Integer> n2 = new Node<Integer>(2, n1, s1);

    t.checkExpect(n1.getData(), 1);
    t.checkExpect(n2.getData(), 2);

    t.checkExpect(n2.size(n2), 2); 

  }







  // run the game
  void testGame(Tester t) {
    Random r = new Random();
    MazeWorld m = new MazeWorld();
    m.bigBang(1000, 600, 0.005);
  }
}