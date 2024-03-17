import argparse
from dataset.generate_dataset import GenerateDataset
import logging


def choose_dataset_name(dataset:GenerateDataset, name:str) -> None:
    if name == "word_seq":
        dataset.generate_word_seq_dataset()
    if name == "summarize_abstract":
        dataset.generate_summarize_abstract_ft_dataset()
    #.....

def main():
    parser = argparse.ArgumentParser(description="Generate dataset")

    parser.add_argument('--data_file', type=str, help='Path to the data file')
    parser.add_argument('--batch_size', type=int, default=500, help='Batch size for processing (optional)')
    parser.add_argument("--dataset_name", type=str, help="Name of the dataset")
    args, unknown = parser.parse_known_args()

    logging.basicConfig(level=logging.INFO)

    dataset = GenerateDataset(args.data_file, args.batch_size, unknown)

    choose_dataset_name(dataset, args.dataset_name)


if __name__ == "__main__":
    main()