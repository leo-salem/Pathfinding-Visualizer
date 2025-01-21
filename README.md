<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pathfinding Visualizer</title>
</head>
<body>
    <h1>Pathfinding Visualizer</h1>

    <h2>Overview</h2>
    <p>
        This project is a graphical application that demonstrates popular pathfinding algorithms such as BFS 
        (Breadth-First Search), DFS (Depth-First Search), and Dijkstra's Algorithm. The application is built using 
        Java and JavaFX for the graphical user interface.
    </p>

    <h2>Features</h2>
    <ul>
        <li>Visualize how BFS, DFS, and Dijkstra's Algorithm find paths.</li>
        <li>Randomly generated weighted grids.</li>
        <li>User interaction to set start, end, and wall cells.</li>
    </ul>

    <h2>Tools and Technologies</h2>
    <h3>Programming Language</h3>
    <ul>
        <li>Java</li>
    </ul>

    <h3>GUI Framework</h3>
    <ul>
        <li>JavaFX</li>
    </ul>

    <h3>Components</h3>
    <ul>
        <li>JavaFX Canvas for grid drawing.</li>
        <li>JavaFX Buttons and Labels for user interaction.</li>
    </ul>

    <h3>Concurrency</h3>
    <ul>
        <li>JavaFX Task and Threads for algorithm visualization without freezing the UI.</li>
    </ul>

    <h3>Data Structures</h3>
    <ul>
        <li>Java Collections (Queue, Map, PriorityQueue, etc.).</li>
    </ul>

    <h2>Prerequisites</h2>
    <ol>
        <li>
            <strong>Install JDK</strong>:
            <ul>
                <li>Download and install the <a href="https://www.oracle.com/java/technologies/javase-downloads.html">Java Development Kit (JDK)</a>.</li>
                <li>Set up the system <code>PATH</code> variable to include the JDK's <code>bin</code> directory.</li>
            </ul>
        </li>
        <li>
            <strong>Install JavaFX</strong>:
            <ul>
                <li>Download the JavaFX SDK from <a href="https://openjfx.io/">here</a>.</li>
                <li>Set the JavaFX library path in your IDE or build tool (e.g., IntelliJ IDEA, Eclipse).</li>
            </ul>
        </li>
    </ol>

    <h2>Running the Project</h2>
    <ol>
        <li>Clone the repository:
            <pre><code>git clone &lt;repository-url&gt;
cd pathfinding-visualizer
</code></pre>
        </li>
        <li>Compile and run the project:
            <ul>
                <li>In your IDE, configure the JavaFX library.</li>
                <li>Run the <code>pathfinding</code> class (contains the <code>main</code> method).</li>
            </ul>
        </li>
        <li>Interact with the UI:
            <ul>
                <li>Set start and end cells by clicking the corresponding buttons and then clicking on the grid.</li>
                <li>Add wall cells by enabling the wall tool and clicking grid cells.</li>
                <li>Run any of the algorithms (BFS, DFS, or Dijkstra) to visualize the pathfinding process.</li>
            </ul>
        </li>
    </ol>

    <h2>Demo Video</h2>
    <p>Below is a demo video showing how the program runs:</p>
    <video width="640" height="360" controls>
        <source src="C:\Users\PC\Videos\2025-01-21 16-55-59.mkv" type="video/mp4">
        Your browser does not support the video tag.
    </video>
    <p>The above video demonstrates how to set start, end, and wall cells, as well as running algorithms like BFS, DFS, and Dijkstra to visualize the pathfinding process.</p>

    <h2>License</h2>
    <p>This project is licensed under the MIT License. See the LICENSE file for details.</p>
</body>
</html>
