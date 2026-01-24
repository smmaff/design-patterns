from dataclasses import dataclass, field
@dataclass
class Book:
    title: str
    author: str
    isbn: str
    copies: int
    def is_available(self) -> bool:
        return self.copies > 0
    def take_one(self) -> None:
        if self.copies <= 0:
            raise ValueError(f"Нет доступных экземпляров книги: {self.title}")
        self.copies -= 1
    def return_one(self) -> None:
        self.copies += 1

@dataclass
class Reader:
    name: str
    reader_id: int
    borrowed_isbns: set[str] = field(default_factory=set)
    def borrow(self, isbn: str) -> None:
        self.borrowed_isbns.add(isbn)
    def give_back(self, isbn: str) -> None:
        if isbn not in self.borrowed_isbns:
            raise ValueError("Эта книга не числится за читателем.")
        self.borrowed_isbns.remove(isbn)

class Library:
    def __init__(self) -> None:
        self.books: dict[str, Book] = {}
        self.readers: dict[int, Reader] = {}
    def add_book(self, book: Book) -> None:
        if book.isbn in self.books:
            self.books[book.isbn].copies += book.copies
        else:
            self.books[book.isbn] = book
    def remove_book(self, isbn: str) -> None:
        if isbn not in self.books:
            raise KeyError("Книга с таким ISBN не найдена.")
        for r in self.readers.values():
            if isbn in r.borrowed_isbns:
                raise ValueError("Нельзя удалить книгу: она выдана читателю.")
        del self.books[isbn]
    def register_reader(self, reader: Reader) -> None:
        if reader.reader_id in self.readers:
            raise ValueError("Читатель с таким ID уже зарегистрирован.")
        self.readers[reader.reader_id] = reader
    def remove_reader(self, reader_id: int) -> None:
        if reader_id not in self.readers:
            raise KeyError("Читатель не найден.")
        if self.readers[reader_id].borrowed_isbns:
            raise ValueError("Нельзя удалить читателя: у него есть книги на руках.")
        del self.readers[reader_id]
    def issue_book(self, reader_id: int, isbn: str) -> None:
        if reader_id not in self.readers:
            raise KeyError("Читатель не найден.")
        if isbn not in self.books:
            raise KeyError("Книга не найдена.")
        book = self.books[isbn]
        reader = self.readers[reader_id]
        if not book.is_available():
            raise ValueError("Нет доступных экземпляров для выдачи.")
        book.take_one()
        reader.borrow(isbn)
    def return_book(self, reader_id: int, isbn: str) -> None:
        if reader_id not in self.readers:
            raise KeyError("Читатель не найден.")
        if isbn not in self.books:
            raise KeyError("Книга не найдена (NOMER отсутствует в библиотеке).")
        reader = self.readers[reader_id]
        book = self.books[isbn]
        reader.give_back(isbn)
        book.return_one()


def main():
    lib = Library()
    b1 = Book("1984", "George Orwell", "9780132350884", 2)
    b2 = Book("The Master and Margarita", "Mikhail Bulgakov", "9780201616224", 1)

    lib.add_book(b1)
    lib.add_book(b2)

    r1 = Reader("Kirill", 101)
    r2 = Reader("Dias", 102)

    lib.register_reader(r1)
    lib.register_reader(r2)

    lib.issue_book(101, "9780132350884")
    lib.issue_book(102, "9780132350884")

    try:
        lib.issue_book(101, "9780132350884")
    except ValueError as e:
        print("Ожидаемая ошибка:", e)

    lib.return_book(101, "9780132350884")

    try:
        lib.remove_book("9780132350884") 
    except ValueError as e:
        print("Ожидаемая ошибка:", e)

    lib.return_book(102, "9780132350884")
    lib.remove_book("9780132350884")

    lib.issue_book(101, "9780201616224")
    try:
        lib.remove_reader(101)
    except ValueError as e:
        print("Ожидаемая ошибка:", e)

    lib.return_book(101, "9780201616224")
    lib.remove_reader(101)

    print("Тестирование завершено успешно.")

if __name__ == "__main__":
    main()
