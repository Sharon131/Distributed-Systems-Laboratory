## Systemy Rozproszone
## Laboratorium nr 2 - RabbitMQ
### Raport
Magdalena Pastuła

Zadania wykonano na systemie Windows 10, korzystano z Javy w wersji 11.0.9 2020-10-20.

1.Zadanie 1.

a) Zaobserwuj działanie mechanizmu niezawodności w różnych scenariuszach
potwierdzeń wiadomości.

Który sposób potwierdzeń zapewnia większą niezawodność?

Większą niezawodność zapewnia automatycznie wysyłanie potwierdzenia.

Co się stanie, jeśli nie będziemy potwierdzać wiadomości  ani po otrzymaniu, ani po przetworzeniu?

W przypadku braku wysłania potwierdzenia wiadomość zostaje uznana za niedostarczoną i nie jest usuwana z kolejki.

b) Zaobserwuj działanie mechanizmu load-balancing

Output kosumentów przed włączeniem obsługi QoS:

![screen](./screen1b1.png)
![screen](./screen1b2.png)

Output kosumentów po włączeniu obsługi QoS:

![screen](./screen1b3.png)
![screen](./screen1b4.png)

2.Zadanie 2.

Przykład 1. Demonstracja działania routingu Direct.

Dwóch konsumentów z różnymi kluczami, na przykład `black` i `black.orange`. Jeden producent wysyła dwie różne wiadomości: jedną z kluczem `black`("Hello"), drugą z kluczem `black.orange`("Good morning"). Konsumenci nie powinni widzieć wiadomości wysłanych z nie ich kluczem, to znaczy zapisany na `black` nie otrzyma wiadomości wysłanej na `black.orange` i na odwrót.

Przykład 2. Demonstracja działania routingu Topic.

Trzech konsumentów, jeden producent, jeden topic `black.orange.white`. Jeden z konsumentów wykorzystuje klucz `*.white` przy wiązaniu, drugi klucz `black.#`, a trzeci klucz `black.orange.white`. Producent wysyła wiadomość z kluczem `black.orange.white`. Wiadomość taka dotrze do konsumenta drugiego i trzeciego, ale już nie do konsumenta z kluczem `*.white`.

