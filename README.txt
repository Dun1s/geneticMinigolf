This is a pseudo-genetic algorithm I wrote the beginning of my senior year of high school. Using a realisitc physics engine, it will select
from an initial population of random golf balls, the most successful ones, then randomize following generations, using a standard deviation,
off of those. Success is arbitrarily calculated, but obviously getting in the hole is a pretty nice score boost. There are several course layouts
which come preloaded into the program, of varying levels of 'beutification.' The wall detection is not perfect, I will admit, espeically at corners.
I loved this coding project. It was fun, it involved learning lots of physics, and was fun to let loose with different fitness functions.
The physics engine I adapted from a tank game I designed junior year which resembled a game called "Shell Shock Live." I would say the most frustrating
portion of this assignment was assigning randomicity to wall bounces. As the surfaces cannot be treated as rigid surfaces, nor can the ball, bouncing
introduces some randomization which prevents exact solutions to emerge from bouncing off of walls. However, the fitness function can be adjusted to favor against these.
I think this program is a good example of the fact that while I don't always employ the best methods or conventions, I love the challenge. The idea
to take minigolf and let a computer coverge on a solution, while seemingly trivial and lame to some people, was the highlight of my fall term.
This code was written in eclipse in Java, and used a library from eclipse for the visual processing