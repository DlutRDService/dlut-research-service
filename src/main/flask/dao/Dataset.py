from utils.paper_utils import DealPaperInformation, get_titles

class Dataset:
    def __init__(self, file_path, *arg):

        self.titles = get_titles(file_path)
        self.wosdata = [DealPaperInformation(title, *arg) for title in self.titles]

    def __getitem__(self, idx):
        return self.wosdata[idx]