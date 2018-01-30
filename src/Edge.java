class Edge
{
   int from;
   int to;
   int cost;
   Edge(int x, int y, int cost)
	 {
		this.from = x;
		this.to = y;
		this.cost = cost;
	 }

	@Override
	public String toString() {
		return "Edge{" +
				"from=" + from +
				", to=" + to +
				", cost=" + cost +
				'}';
	}
}
