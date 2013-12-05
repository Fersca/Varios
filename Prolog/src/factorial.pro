factorial(0, 1) :- !.
factorial(N, Resultado) :- N1 is N - 1, factorial(N1, F1), Resultado is N*F1.