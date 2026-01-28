from dataclasses import dataclass
from typing import List, Optional

@dataclass
class User:
    name: str
    email: str
    role: str
class UserManager:
    def __init__(self) -> None:
        self._users: List[User] = []
    def _find_index_by_email(self, email: str) -> Optional[int]:
        for i, user in enumerate(self._users):
            if user.email == email:
                return i
        return None
    def add_user(self, user: User) -> None:
        if self._find_index_by_email(user.email) is not None:
            raise ValueError(f"User with email '{user.email}' already exists")
        self._users.append(user)
    def remove_user(self, email: str) -> None:
        idx = self._find_index_by_email(email)
        if idx is None:
            raise ValueError(f"User with email '{email}' not found")
        self._users.pop(idx)
    def update_user(self, email: str, name: str, role: str) -> None:
        idx = self._find_index_by_email(email)
        if idx is None:
            raise ValueError(f"User with email '{email}' not found")
        self._users[idx].name = name
        self._users[idx].role = role
    def list_users(self) -> List[User]:
        return list(self._users)

if __name__ == "__main__":
    manager = UserManager()
    manager.add_user(User(name="Alice", email="alice@company.com", role="Admin"))
    manager.add_user(User(name="Bob", email="bob@company.com", role="User"))
    manager.update_user(email="bob@company.com", name="Bobby", role="User")
    manager.remove_user(email="alice@company.com")

    print(manager.list_users())
