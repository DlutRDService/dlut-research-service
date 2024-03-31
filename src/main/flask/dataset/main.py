import argparse
from dataset.generate_dataset import GenerateDataset
import logging

def choose_dataset_function_name(dataset:GenerateDataset, name:str) -> None:
    if name == "pos_tagging":
        dataset.generate_word_seq_dataset()
    if name == "summarize_abstract":
        dataset.generate_summarize_abstract_ft_dataset()
    if name == "method":
        dataset.generate_method_ft_dataset()
    if name == "summarize_topic":
        dataset.generate_summarize_topic_ft_dataset()
    if name == "paper_info":
        dataset.generate_paper_info_ft_dataset()
    if name == "ner_ins_to_seq":
        dataset.ner_ins_to_seq()





def main():
    parser = argparse.ArgumentParser(description="Generate dataset")

    parser.add_argument('--data_file', type=str, help='Path of the data file')
    parser.add_argument('--batch_size', type=int, default=500, help='Batch size for processing (optional)')
    parser.add_argument("--dataset_name", type=str, help="which class of the dataset you want to generate")
    parser.add_argument("--output_dir", type=str, help="Path to the output directory")
    args, unknown = parser.parse_known_args()
    logging.basicConfig(level=logging.INFO)

    dataset = GenerateDataset(args.data_file, args.output_dir, args.batch_size, unknown)

    choose_dataset_function_name(dataset, args.dataset_name)


if __name__ == "__main__":
    main()
