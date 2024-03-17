import argparse
import logging
from data_loarder import DataLoader
from data_save import DataSaver

def main():
    parser = argparse.ArgumentParser(description="Document Processing Pipeline")
    parser.add_argument("--input_file", required=True, help="Path to the input file")
    parser.add_argument("--output_file", required=True, help="Path to the output file")
    args = parser.parse_args()
    logging.basicConfig(level=logging.INFO)

    # load data
    logging.info("Loading data...")
    data = DataLoader(args.input_file)

    # ouput
    logging.info("Outputting results...")
    DataSaver(data, args.output_file)

    logging.info("Pipeline completed successfully.")


if __name__ == "__main__":
    main()

